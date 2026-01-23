package com.azzitech.saas_service.dto;

import java.util.List;

public record Schedule(String dayName, String focus, List<Exercise> exercises) {
}
