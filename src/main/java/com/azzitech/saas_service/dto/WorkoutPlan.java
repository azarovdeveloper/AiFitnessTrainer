package com.azzitech.saas_service.dto;

import java.util.List;

public record WorkoutPlan(String title, String summary, int weeks, List<Schedule> schedule) {
}
