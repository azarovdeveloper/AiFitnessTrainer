package com.azzitech.saas_service.controller;

import com.azzitech.saas_service.service.AiVideoService;
import com.google.genai.types.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class AiVideoController {

    private final AiVideoService aiVideoService;

    @PostMapping(value = "/generate", produces = "application/octet-stream")
    public ResponseEntity<byte[]> create(@RequestPart String prompt,
                                         @RequestPart(required = false) MultipartFile file) {

        byte[] generated = aiVideoService.generate(prompt, fetchImage(file));

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

    private Image fetchImage(MultipartFile file) {
        Image image = null;
        if (file != null && !file.isEmpty()) {
            try {
                image = Image.builder()
                        .imageBytes(file.getBytes())
                        .mimeType(file.getContentType())
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return image;
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
