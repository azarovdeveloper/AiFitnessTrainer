package com.azzitech.saas_service.dao.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AiChatMessage {
    @Id
    private UUID id;
    @ManyToOne(optional = false)
    private AiChatSession session;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AiChatRole role;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private Instant createdAt;

    public static AiChatMessage create(AiChatSession session, AiChatRole role, String content) {
        AiChatMessage aiChatMessage = new AiChatMessage();
        aiChatMessage.id = UUID.randomUUID();
        aiChatMessage.session = session;
        aiChatMessage.role = role;
        aiChatMessage.content = content;
        aiChatMessage.createdAt = Instant.now();
        return aiChatMessage;
    }
}

