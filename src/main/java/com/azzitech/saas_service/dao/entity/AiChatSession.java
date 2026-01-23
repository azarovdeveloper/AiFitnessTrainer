package com.azzitech.saas_service.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AiChatSession {

    @Id
    private UUID id;
    @Column(nullable = false)
    private Instant createdAt;
    @Setter
    @ManyToOne
    private PromptTemplate promptTemplate;

    public static AiChatSession create() {
        AiChatSession session = new AiChatSession();
        session.id = UUID.randomUUID();
        session.createdAt = Instant.now();
        return session;
    }
}

