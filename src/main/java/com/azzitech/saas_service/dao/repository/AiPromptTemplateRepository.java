package com.azzitech.saas_service.dao.repository;

import com.azzitech.saas_service.dao.entity.PromptTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiPromptTemplateRepository extends JpaRepository<PromptTemplate, UUID> {
    PromptTemplate findByName(String name);
}
