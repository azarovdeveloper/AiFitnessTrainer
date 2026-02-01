package com.azzitech.saas_service.client.ai;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.stereotype.Service;

@Service
public class VeoVideoClient {

    private final Client client;

    public VeoVideoClient() {
        this.client = Client.builder()
                .vertexAI(true)
                .location("global")
                .build();
    }

    public GenerateVideosOperation startVideoGeneration(String prompt, Image image) {
        GenerateVideosConfig config =
                GenerateVideosConfig.builder()
                        .aspectRatio("9:16")
                        .durationSeconds(8)     // Veo prefers short clips
                        .fps(24)
                        .build();
        return client.models.generateVideos(
                "veo-3.0-generate-preview",
                prompt,
                image,
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

