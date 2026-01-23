package com.azzitech.saas_service.utils;

public class ResponseUtil {

    public static String extractJson(String text) {
        if (text == null) return null;

        text = text.trim();

        if (text.startsWith("```")) {
            int firstNewLine = text.indexOf('\n');
            if (firstNewLine > 0) {
                text = text.substring(firstNewLine + 1);
            }
            if (text.endsWith("```")) {
                text = text.substring(0, text.length() - 3);
            }
        }

        return text.trim();
    }
}
