package com.taskflow.task_service.task;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
	
	  private final TaskRepository repository;

	    public TaskService(TaskRepository repository) {
	        this.repository = repository;
	    }

	    
	    //-----------------Methods---------------------------------//
	    public TaskResponse create(CreateTaskRequest request) {
	        Task task = new Task(request.title(), request.description(), TaskStatus.TODO, request.dueDate());
	        return TaskResponse.from(repository.save(task));
	    }
	    
	    public List<TaskResponse> findAll(){
	    	return repository.findAll().stream().map(TaskResponse::from).toList();
	    }
	    
	    
	    public TaskResponse findById(Long id) {
	    	Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	    	return TaskResponse.from(task);
	    }
	    
	    public TaskResponse editTask(Long id, EditTaskRequest taskToEdit) {
	    	Task existingTask = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	    	
	    	existingTask.setTitle(taskToEdit.title());
	    	existingTask.setDescription(taskToEdit.description());
	    	existingTask.setStatus(taskToEdit.status());
	    	existingTask.setDueDate(taskToEdit.dueDate());
	    	
	    	Task updatedTask = repository.save(existingTask);
	    	
	    	return TaskResponse.from(updatedTask); 
	    	
	    }
	    
	    public void delete(Long id) {
	        if (!repository.existsById(id)) {
	            throw new TaskNotFoundException(id);
	        }
	        repository.deleteById(id);
	    }

}
