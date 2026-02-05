package com.azzitech.saas_service.controller;

import com.azzitech.saas_service.service.AiVideoService;
import com.google.genai.types.Image;
import com.google.genai.types.VideoGenerationReferenceImage;
import com.google.genai.types.VideoGenerationReferenceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class AiVideoController {

    private final AiVideoService aiVideoService;

    @PostMapping(value = "/generate", produces = "application/octet-stream")
    public ResponseEntity<byte[]> create(@RequestPart String prompt,
                                         @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        byte[] generated = aiVideoService.generate(prompt, fetchImages(files));

        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(UUID.randomUUID() + ".mp4")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.valueOf("video/mp4"))
                .contentLength(generated.length)
                .body(generated);
    }

    private List<VideoGenerationReferenceImage> fetchImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        if (files.size() > 3) {
            throw new IllegalArgumentException("Up to 3 reference images are allowed.");
        }
        List<VideoGenerationReferenceImage> references = new ArrayList<>();
        files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .forEach(file -> {
                    try {
                        Image image = Image.builder()
                                .imageBytes(file.getBytes())
                                .mimeType(file.getContentType())
                                .build();
                        references.add(VideoGenerationReferenceImage.builder()
                                .image(image)
                                .referenceType(new VideoGenerationReferenceType(VideoGenerationReferenceType.Known.ASSET))
                                .build());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return references;
    }

    @GetMapping("/{id}/source")
    public ResponseEntity<byte[]> getSource(@PathVariable UUID id) {
        Image source = aiVideoService.getSource(id);

        if (source.imageBytes().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        byte[] image = source.imageBytes().get();
        MediaType contentType = source.mimeType().isPresent()
                ? MediaType.valueOf(source.mimeType().get())
                : MediaType.IMAGE_PNG;

        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(id + "." + contentType.getSubtype())
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(contentType)
                .contentLength(image.length)
                .body(image);
    }
}
