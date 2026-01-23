package com.azzitech.saas_service.service;

import com.azzitech.saas_service.client.ai.GeminiClient;
import com.azzitech.saas_service.dao.entity.AiChatMessage;
import com.azzitech.saas_service.dao.entity.AiChatRole;
import com.azzitech.saas_service.dao.entity.AiChatSession;
import com.azzitech.saas_service.dao.repository.AiSessionRepository;
import com.azzitech.saas_service.utils.ResponseSchemaUtil;
import com.azzitech.saas_service.utils.ResponseUtil;
import com.google.genai.types.Content;
import com.google.genai.types.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiWorkoutPlanGenService {

    private final AiSessionRepository aiSessionRepo;
    private final AiMessageService aiMessageService;
    private final GeminiClient gemini;

    @Transactional
    public String generateWorkoutPlan(UUID sessionId, String text) {
        AiChatSession session = aiSessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("sessionId not found"));
        aiMessageService.save(AiChatMessage.create(session, AiChatRole.USER, text));

        List<Content> contents = aiMessageService.collectSessionHistory(session);
        if (session.getPromptTemplate().getResponseSchema() == null) {
            throw new IllegalArgumentException("The schema expected");
        }
        if (session.getPromptTemplate().getResponseMimeType() == null) {
            session.getPromptTemplate().setResponseMimeType("application/json");
        }
        Schema schema = ResponseSchemaUtil.get(session.getPromptTemplate().getResponseSchema());
        String answer = gemini.generateJson(contents, session.getPromptTemplate().getResponseMimeType(), schema);
        String json = ResponseUtil.extractJson(answer);
        aiMessageService.save(AiChatMessage.create(session, AiChatRole.ASSISTANT, json));
        ResponseSchemaUtil.validate(json, schema);
        return json;
    }
}
