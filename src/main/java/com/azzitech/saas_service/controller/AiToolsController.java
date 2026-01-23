package com.azzitech.saas_service.controller;

import com.azzitech.saas_service.service.AiWorkoutPlanGenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tool")
@RequiredArgsConstructor
public class AiToolsController {

    private final AiWorkoutPlanGenService aiWorkoutPlanGenService;

    @PostMapping("/{id}/plan-Generation")
    public Map<String, Object> planGenerator( @PathVariable UUID id, @RequestBody Map<String, String> body) {
        return Map.of("answer", aiWorkoutPlanGenService.generateWorkoutPlan(id, body.get("questionnaire")));
    }
}

