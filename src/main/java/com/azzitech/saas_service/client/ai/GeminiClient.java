package com.azzitech.saas_service.client.ai;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiClient {

    private final Client client;

    public GeminiClient() {
        this.client = Client.builder()
                .vertexAI(true)
                .location("global")
                .build();
    }

    public String chat(List<Content> history) {
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        history,
                        GenerateContentConfig.builder().build()
                );
        return response.text();
    }

    public String generateJson(List<Content> history, String format, Schema schema) {
        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .responseMimeType(format)
                        .responseSchema(schema)
                        .build();
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3-flash-preview",
                        history,
                        config
                );
        return response.text();
    }
}
