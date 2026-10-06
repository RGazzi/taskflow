package com.taskflow.task_service;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.taskflow.task_service.task.TaskNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(TaskNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex){
	    String message = ex.getBindingResult().getFieldError().getDefaultMessage();
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(Map.of("error", message));
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, String>> handleLogin(BadCredentialsException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("error", "Invalid email or password"));
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateEmail(DataIntegrityViolationException ex) {
	    return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(Map.of("error", "Email already registered"));
	}
	

}
