package com.azzitech.saas_service.service;

import com.azzitech.saas_service.dao.entity.AiChatMessage;
import com.azzitech.saas_service.dao.entity.AiChatRole;
import com.azzitech.saas_service.dao.entity.AiChatSession;
import com.azzitech.saas_service.dao.repository.AiChatMessageRepository;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiMessageService {

    private final AiChatMessageRepository aiMessageRepo;

    @Transactional
    public List<Content> collectSessionHistory(AiChatSession session) {
        List<Content> contents = new ArrayList<>();
        for (AiChatMessage m : aiMessageRepo.findBySessionOrderByCreatedAt(session)) {
            contents.add(Content.builder()
                    .role(m.getRole() == AiChatRole.ASSISTANT ? "model" : "user")
                    .parts(Optional.ofNullable(Part.fromText(m.getContent())).stream().toList())
                    .build());
        }
        return contents;
    }

    @Transactional
    public AiChatMessage save(AiChatMessage aiChatMessage) {
        return aiMessageRepo.save(aiChatMessage);
    }
}
