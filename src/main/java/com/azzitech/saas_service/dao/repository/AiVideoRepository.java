package com.azzitech.saas_service.dao.repository;

import com.azzitech.saas_service.dao.entity.AiVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AiVideoRepository extends JpaRepository<AiVideo, UUID> {

    Optional<SourceAndMimeType> findSourceAndMimeTypeById(UUID id);

    interface SourceAndMimeType {
        byte[] getSource();
        String getSourceMimeType();
    }
}
