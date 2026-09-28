package com.taskflow.task_service.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository repository;

	@InjectMocks
	private TaskService taskService;

	// ---------- findById ----------

	@Test
	void shouldReturnTaskWhenFound() {
		// given
		Task task = new Task("Learn Mockito", "desc", TaskStatus.TODO, LocalDate.now());
		when(repository.findById(1L)).thenReturn(Optional.of(task));

		// when
		TaskResponse response = taskService.findById(1L);

		// then
		assertThat(response.title()).isEqualTo("Learn Mockito");
	}

	@Test
	void shouldThrowWhenTaskNotFound() {
		// given
		when(repository.findById(99L)).thenReturn(Optional.empty());

		// when / then
		assertThatThrownBy(() -> taskService.findById(99L))
				.isInstanceOf(TaskNotFoundException.class)
				.hasMessageContaining("99");
	}

	// ---------- create ----------

	@Test
	void shouldCreateTaskWithTodoStatus() {
		// given
		CreateTaskRequest request = new CreateTaskRequest("New task", "desc", LocalDate.now());
		when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		TaskResponse response = taskService.create(request);

		// then
		assertThat(response.status()).isEqualTo(TaskStatus.TODO);
		verify(repository, times(1)).save(any(Task.class));
	}

	// ---------- delete ----------

	@Test
	void shouldDeleteTaskWhenItExists() {
		// given
		when(repository.existsById(1L)).thenReturn(true);

		// when
		taskService.delete(1L);

		// then
		verify(repository, times(1)).deleteById(1L);
	}

	@Test
	void shouldThrowWhenDeletingNonExistentTask() {
		// given
		when(repository.existsById(99L)).thenReturn(false);

		// when / then
		assertThatThrownBy(() -> taskService.delete(99L))
				.isInstanceOf(TaskNotFoundException.class)
				.hasMessageContaining("99");

		verify(repository, never()).deleteById(any());
	}

	// ---------- editTask ----------

	@Test
	void shouldUpdateAllFieldsWhenEditingExistingTask() {
		// given
		Task existing = new Task("Old title", "old desc", TaskStatus.TODO, LocalDate.of(2026, 10, 1));
		when(repository.findById(1L)).thenReturn(Optional.of(existing));
		when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

		EditTaskRequest request = new EditTaskRequest(
				"New title", "new desc", TaskStatus.DOING, LocalDate.of(2026, 12, 1));

		// when
		TaskResponse response = taskService.editTask(1L, request);

		// then
		assertThat(response.title()).isEqualTo("New title");
		assertThat(response.description()).isEqualTo("new desc");
		assertThat(response.status()).isEqualTo(TaskStatus.DOING);
		assertThat(response.dueDate()).isEqualTo(LocalDate.of(2026, 12, 1));
		verify(repository, times(1)).save(existing);
	}

	@Test
	void shouldThrowWhenEditingNonExistentTask() {
		// given
		when(repository.findById(99L)).thenReturn(Optional.empty());
		EditTaskRequest request = new EditTaskRequest(
				"Title", "desc", TaskStatus.DONE, LocalDate.of(2026, 12, 1));

		// when / then
		assertThatThrownBy(() -> taskService.editTask(99L, request))
				.isInstanceOf(TaskNotFoundException.class)
				.hasMessageContaining("99");

		verify(repository, never()).save(any(Task.class));
	}
}