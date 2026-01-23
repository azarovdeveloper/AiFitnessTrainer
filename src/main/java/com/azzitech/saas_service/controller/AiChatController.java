package com.azzitech.saas_service.controller;

import com.azzitech.saas_service.service.AiChatService;
import com.azzitech.saas_service.service.AiWorkoutPlanGenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;
    private final AiWorkoutPlanGenService aiWorkoutPlanGenService;



    @PostMapping("/{id}")
    public Map<String, String> send(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        return Map.of("answer", aiChatService.sendMessage(id, body.get("message")));
    }

    @PostMapping("/{id}/generate-plan")
    public Map<String, Object> generatePlan( @PathVariable UUID id, @RequestBody Map<String, String> body) {
        return Map.of("answer", aiWorkoutPlanGenService.generateWorkoutPlan(id, body.get("questionnaire")));
    }
}

