package com.azzitech.saas_service.utils;

import java.util.Map;

public class PromptUtil {

    public  static String render(String template, Map<String, Object> params) {
        String result = template;
        for (var entry : params.entrySet()) {
            result = result.replace(
                    "{{profile." + entry.getKey() + "}}",
                    String.valueOf(entry.getValue())
            );
        }
        return result;
    }
}
