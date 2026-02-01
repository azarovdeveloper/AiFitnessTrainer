package com.azzitech.saas_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import java.util.List;
import java.util.Map;

public class ResponseSchemaUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Schema get(String key) {
        return switch (key) {

            case "workoutPlan" -> Schema.builder()
                    .type(Type.Known.OBJECT)
                    .properties(Map.of(
                            "title", Schema.builder().type(Type.Known.STRING).build(),
                            "summary", Schema.builder().type(Type.Known.STRING).build(),
                            "weeks", Schema.builder().type(Type.Known.NUMBER).build(),
                            "schedule", Schema.builder()
                                    .type(Type.Known.ARRAY)
                                    .items(
                                            Schema.builder()
                                                    .type(Type.Known.OBJECT)
                                                    .properties(Map.of(
                                                            "dayName", Schema.builder().type(Type.Known.STRING).build(),
                                                            "focus", Schema.builder().type(Type.Known.STRING).build(),
                                                            "exercises", Schema.builder()
                                                                    .type(Type.Known.ARRAY)
                                                                    .items(
                                                                            Schema.builder()
                                                                                    .type(Type.Known.OBJECT)
                                                                                    .properties(Map.of(
                                                                                            "name", Schema.builder().type(Type.Known.STRING).build(),
                                                                                            "description", Schema.builder().type(Type.Known.STRING).build(),
                                                                                            "sets", Schema.builder().type(Type.Known.NUMBER).build(),
                                                                                            "reps", Schema.builder().type(Type.Known.STRING).build(),
                                                                                            "rest", Schema.builder().type(Type.Known.STRING).build(),
                                                                                            "videoQuery", Schema.builder().type(Type.Known.STRING).build()
                                                                                    ))
                                                                                    .required(List.of("name", "description", "sets", "reps", "rest", "videoQuery"))
                                                                                    .build()
                                                                    )
                                                                    .build()
                                                    ))
                                                    .required(List.of("dayName", "focus", "exercises"))
                                                    .build()
                                    )
                                    .build()
                    ))
                    .required(List.of("title", "summary", "weeks", "schedule"))
                    .build();

            default -> throw new IllegalArgumentException("Unknown schema: " + key);
        };
    }

    public static void validate(String json, Schema schema) {
        try {
            JsonNode node = objectMapper.readTree(json);
            validateNode(node, schema, "$");
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }

    private static void validateNode(JsonNode node, Schema schema, String path) {
        Type type = schema.type().orElseThrow(() -> new IllegalArgumentException("Path: " + path + " missing type"));
        if (Type.Known.OBJECT.equals(type.knownEnum())) {
            if (!node.isObject()) {
                throw new IllegalArgumentException("Path: " + path + " expected OBJECT but found " + node.getNodeType());
            }
            schema.required().ifPresent(
                   items -> items.forEach(req -> {
                       if (!node.has(req)) {
                           throw new IllegalArgumentException("Path: " + path + " missing required field: " + req);
                       }
                   })
            );

            schema.properties().ifPresent(props -> props.forEach((propName, value) -> {
                if (node.has(propName)) {
                    validateNode(node.get(propName), value, path + "." + propName);
                }
            }));
        } else if (Type.Known.ARRAY.equals(type.knownEnum())) {
            if (!node.isArray()) {
                throw new IllegalArgumentException("Path: " + path + " expected ARRAY but found " + node.getNodeType());
            }
            if (schema.items().isPresent()) {
                int i = 0;
                for (JsonNode item : node) {
                    validateNode(item, schema.items().get(), path + "[" + i + "]");
                    i++;
                }
            }
        } else if (Type.Known.STRING.equals(type.knownEnum()) && !node.isTextual()) {
            throw new IllegalArgumentException("Path: " + path + " expected STRING but found " + node.getNodeType());
        } else if (Type.Known.NUMBER.equals(type.knownEnum()) && !node.isNumber()) {
            throw new IllegalArgumentException("Path: " + path + " expected NUMBER but found " + node.getNodeType());
        } else if (Type.Known.BOOLEAN.equals(type.knownEnum()) && !node.isBoolean()) {
            throw new IllegalArgumentException("Path: " + path + " expected BOOLEAN but found " + node.getNodeType());
        } else if (Type.Known.INTEGER.equals(type.knownEnum()) && !node.isIntegralNumber()) {
            throw new IllegalArgumentException("Path: " + path + " expected INTEGER but found " + node.getNodeType());
        }

        // Ensure we don't silently skip validation for types we haven't implemented
        if (!Type.Known.OBJECT.equals(type.knownEnum()) && !Type.Known.ARRAY.equals(type.knownEnum()) &&
            !Type.Known.STRING.equals(type.knownEnum()) && !Type.Known.NUMBER.equals(type.knownEnum()) &&
            !Type.Known.BOOLEAN.equals(type.knownEnum()) && !Type.Known.INTEGER.equals(type.knownEnum())) {
            throw new IllegalArgumentException("Path: " + path + " unhandled schema type: " + type);
        }
    }
}