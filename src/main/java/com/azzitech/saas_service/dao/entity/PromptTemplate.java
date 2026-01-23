package com.azzitech.saas_service.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PromptTemplate {

    @Id
    private UUID id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String text;
    @Setter
    private String responseMimeType;
    private String responseSchema;

}

