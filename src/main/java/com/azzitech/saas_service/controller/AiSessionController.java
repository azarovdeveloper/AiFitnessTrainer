package com.azzitech.saas_service.controller;


import com.azzitech.saas_service.service.AiSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class AiSessionController {

    private final AiSessionService aiSessionService;

    @PostMapping()
    public Map<String, String> create(@RequestParam(required = false) String prompt,
                                      @RequestBody(required = false) Map<String, Object> params) {
        return Map.of("sessionId", aiSessionService.createSession(prompt, params).toString());
    }
}
