package com.taskflow.task_service.task;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditTaskRequest(
    @NotBlank String title,
    String description,
    @NotNull TaskStatus status,
    LocalDate dueDate
) {}