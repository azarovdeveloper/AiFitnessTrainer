package com.azzitech.saas_service.dto;

import java.util.List;

public record CrossfitConditioning(String type, String durationOrRounds, List<String> exercises, String techniqueNotes) {
}
