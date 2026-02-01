package com.azzitech.saas_service.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AiVideo {
    @Id
    private UUID id;
    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] video;
    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] source;
    private String sourceMimeType;
    @Setter
    private String rating;
    @Column(nullable = false)
    private Instant createdAt;

    public static AiVideo create(byte[] video, String sourceMimeType, byte[] source) {
        AiVideo aiVideo = new AiVideo();
        aiVideo.id = UUID.randomUUID();
        aiVideo.video = video;
        aiVideo.source = source;
        aiVideo.sourceMimeType = sourceMimeType;
        aiVideo.createdAt = Instant.now();
        return aiVideo;
    }
}
