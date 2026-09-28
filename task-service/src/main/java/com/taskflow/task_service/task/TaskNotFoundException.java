package com.taskflow.task_service.task;

public class TaskNotFoundException extends RuntimeException{
	public TaskNotFoundException(Long id) {
		super("Task not found with id: " + id);
	}
}
