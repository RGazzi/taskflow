package com.taskflow.task_service.task;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
	    @NotBlank String title,
	    String description,
	    LocalDate dueDate
	) {}
