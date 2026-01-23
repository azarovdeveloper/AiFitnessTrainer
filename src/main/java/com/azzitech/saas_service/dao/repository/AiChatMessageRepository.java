package com.azzitech.saas_service.dao.repository;

import com.azzitech.saas_service.dao.entity.AiChatMessage;
import com.azzitech.saas_service.dao.entity.AiChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, UUID> {
    List<AiChatMessage> findBySessionOrderByCreatedAt(AiChatSession session);
}

