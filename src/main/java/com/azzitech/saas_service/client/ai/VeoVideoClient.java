package com.azzitech.saas_service.client.ai;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeoVideoClient {

    private final Client client;

    public VeoVideoClient() {
        this.client = Client.builder()
                .vertexAI(true)
                .location("global")
                .build();
    }

    public GenerateVideosOperation startVideoGeneration(String prompt, List<VideoGenerationReferenceImage> refs) {
        GenerateVideosConfig config =
                GenerateVideosConfig.builder()
                        .numberOfVideos(1)
                        .durationSeconds(8)
                        .aspectRatio("9:16")
//                        .outputGcsUri("gs://YOUR_BUCKET/output/")
                        .fps(24)
                        .referenceImages(refs)
                        .build();

        return client.models.generateVideos(
                "veo-3.1-generate-preview",
                prompt,
                null,
                config
        );
    }

    public GenerateVideosResponse pollUntilDone(GenerateVideosOperation operation, GetOperationConfig pollConfig) {

        while (operation.done().isEmpty() || !operation.done().get()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Polling interrupted", e);
            }
            operation = client.operations.getVideosOperation(
                    operation,
                    pollConfig
            );
        }
        if (operation.error().isPresent()) {
            throw new IllegalStateException(
                    "Video generation failed: " + operation.error().get()
            );
        }

        return operation.response()
                .orElseThrow(() ->
                        new IllegalStateException("Missing video response"));
    }
}

