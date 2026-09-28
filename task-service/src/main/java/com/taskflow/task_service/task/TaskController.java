package com.taskflow.task_service.task;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@PostMapping
	public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request){
		TaskResponse created = taskService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@GetMapping
	public List<TaskResponse> listAll(){
		return taskService.findAll();
	}
	
	@GetMapping("/{id}")
	public TaskResponse findById(@PathVariable Long id) {
		return taskService.findById(id);
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity<TaskResponse> edit(@PathVariable Long id, @Valid @RequestBody EditTaskRequest taskToEdit){
		TaskResponse updatedTask = taskService.editTask(id, taskToEdit);
		return ResponseEntity.ok(updatedTask);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		taskService.delete(id);
		return ResponseEntity.noContent().build();
	}
	

}
