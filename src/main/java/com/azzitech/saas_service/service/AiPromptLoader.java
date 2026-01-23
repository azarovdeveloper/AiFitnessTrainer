package com.azzitech.saas_service.service;

import com.azzitech.saas_service.dao.entity.PromptTemplate;
import com.azzitech.saas_service.dao.repository.AiPromptTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AiPromptLoader {

    private final AiPromptTemplateRepository repo;

    @PostConstruct
    void load() {
        Yaml yaml = new Yaml();
        InputStream is = getClass().getResourceAsStream("/prompts.yml");
        Map<String, Object> data = yaml.load(is);

        List<Map<String, String>> prompts = (List<Map<String, String>>) data.get("prompts");

        for (var p : prompts) {
            if (repo.findByName(p.get("name")) == null) {
                repo.save(new PromptTemplate(UUID.randomUUID(), p.get("name"), p.get("text"), p.get("responseFormat"), p.get("responseSchemaKey")));
            }
        }
    }
}

