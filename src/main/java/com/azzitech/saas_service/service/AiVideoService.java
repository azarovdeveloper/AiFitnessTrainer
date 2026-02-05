package com.azzitech.saas_service.service;

import com.azzitech.saas_service.client.ai.VeoVideoClient;
import com.azzitech.saas_service.dao.entity.AiVideo;
import com.azzitech.saas_service.dao.repository.AiVideoRepository;
import com.google.genai.types.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiVideoService {

    private final VeoVideoClient veoVideoClient;
    private final AiVideoRepository repository;

    public byte[] generate(String prompt, List<VideoGenerationReferenceImage> refs) {
        log.debug("Generating video for prompt: {}", prompt);
        GenerateVideosOperation operation = veoVideoClient.startVideoGeneration(prompt, refs);

        // Poll the operation status until the video is ready.
        GenerateVideosResponse videosResponse = veoVideoClient.pollUntilDone(operation, GetOperationConfig.builder().build());
        log.debug("Video generated: {}", videosResponse);

        if (videosResponse == null || videosResponse.generatedVideos().isEmpty()) {
            throw new IllegalStateException("No videos generated");
        }
        Video video = videosResponse.generatedVideos()
                .get()
                .getFirst()
                .video()
                .orElseThrow(() -> new IllegalStateException("No video found"));

        Image sourceImage = null;
        if (refs != null && !refs.isEmpty()) {
            VideoGenerationReferenceImage firstRef = refs.getFirst();
            if (firstRef.image().isPresent() && firstRef.image().get().imageBytes().isPresent()) {
                sourceImage = firstRef.image().get();
            }
        }

        if (video.videoBytes().isPresent()) {
            byte[] bytes = video.videoBytes().get();
            byte[] source = sourceImage != null && sourceImage.imageBytes().isPresent() ? sourceImage.imageBytes().get() : null;
            String sourceMimeType = sourceImage != null && sourceImage.mimeType().isPresent() ? sourceImage.mimeType().get() : null;
            repository.save(AiVideo.create(bytes, sourceMimeType, source));
            return bytes;
        }
        if (video.uri().isPresent()) {
            return downloadVideo(video.uri().get());
        }
        throw new IllegalStateException("No video found");
    }

    public Image getSource(UUID id) {
        AiVideoRepository.SourceAndMimeType videoNotFound = repository.findSourceAndMimeTypeById(id)
                .orElseThrow(() -> new IllegalStateException("Video not found"));
        return Image.builder()
                .imageBytes(videoNotFound.getSource())
                .mimeType(videoNotFound.getSourceMimeType())
                .build();
    }

    private byte[] downloadVideo(String uri) {
        log.debug("Downloading video from URI: {}", uri);
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();
            HttpResponse<byte[]> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (httpResponse.statusCode() != 200) {
                throw new IllegalStateException("Failed to download video, status: " + httpResponse.statusCode());
            }
            return httpResponse.body();
        } catch (Exception e) {
            throw new IllegalStateException("Video download failed", e);
        }
    }
}
