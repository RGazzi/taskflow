package com.taskflow.task_service.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		@Email @NotBlank String email,
		@Size(min=8) String password) {}
