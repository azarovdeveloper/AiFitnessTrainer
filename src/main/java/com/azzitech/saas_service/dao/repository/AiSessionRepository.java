package com.azzitech.saas_service.dao.repository;

import com.azzitech.saas_service.dao.entity.AiChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiSessionRepository extends JpaRepository<AiChatSession, UUID> {}

