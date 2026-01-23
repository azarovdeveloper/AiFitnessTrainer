package com.azzitech.saas_service.service;

import com.azzitech.saas_service.client.ai.GeminiClient;
import com.azzitech.saas_service.dao.entity.AiChatMessage;
import com.azzitech.saas_service.dao.entity.AiChatRole;
import com.azzitech.saas_service.dao.entity.AiChatSession;
import com.azzitech.saas_service.dao.repository.AiSessionRepository;
import com.google.genai.types.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final AiSessionRepository aiSessionRepo;
    private final AiMessageService aiMessageService;
    private final GeminiClient gemini;


    @Transactional
    public String sendMessage(UUID sessionId, String text) {
        AiChatSession session = aiSessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("sessionId not found"));
        aiMessageService.save(AiChatMessage.create(session, AiChatRole.USER, text));

        List<Content> contents = aiMessageService.collectSessionHistory(session);

        String answer = gemini.chat(contents);
        aiMessageService.save(AiChatMessage.create(session, AiChatRole.ASSISTANT, answer));
        return answer;
    }

}

