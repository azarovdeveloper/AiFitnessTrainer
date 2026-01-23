package com.azzitech.saas_service.service;

import com.azzitech.saas_service.dao.entity.AiChatMessage;
import com.azzitech.saas_service.dao.entity.AiChatRole;
import com.azzitech.saas_service.dao.entity.AiChatSession;
import com.azzitech.saas_service.dao.repository.AiChatMessageRepository;
import com.azzitech.saas_service.dao.repository.AiPromptTemplateRepository;
import com.azzitech.saas_service.dao.repository.AiSessionRepository;
import com.azzitech.saas_service.utils.PromptUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiSessionService {

    private final AiSessionRepository aiSessionRepo;
    private final AiChatMessageRepository aiMessageRepo;
    private final AiPromptTemplateRepository aiPromptRepo;

    @Transactional
    public UUID createSession(String promptName, Map<String, Object> params) {
        AiChatSession session = AiChatSession.create();
        if (promptName != null) {
            session.setPromptTemplate(aiPromptRepo.findByName(promptName));
        }
        aiSessionRepo.save(session);

        if (session.getPromptTemplate() != null) {
            if ( params != null && !params.isEmpty()) {
                String renderedPrompt = PromptUtil.render(session.getPromptTemplate().getText(), params);
                aiMessageRepo.save(AiChatMessage.create(session, AiChatRole.SYSTEM, renderedPrompt));
            } else {
                aiMessageRepo.save(AiChatMessage.create(session, AiChatRole.SYSTEM, session.getPromptTemplate().getText()));
            }
        }
        return session.getId();
    }
}
