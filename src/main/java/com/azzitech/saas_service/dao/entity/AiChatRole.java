package com.azzitech.saas_service.dao.entity;

public enum AiChatRole {

    USER("USER"), ASSISTANT("ASSISTANT"), SYSTEM("SYSTEM");

    private final String role;

    AiChatRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
