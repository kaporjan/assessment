package com.blackswan.assesment.task.controller;

import com.blackswan.assesment.task.api.Task;
import com.blackswan.assesment.task.api.Tasks;
import com.blackswan.assesment.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/task")
public class TaskController {
	private TaskService taskService;

	public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}

	@PostMapping
	public Long createTask(@PathVariable Long userId, @RequestBody Task task) {
		return taskService.createTask(userId, task);
	}

	@PutMapping("/{id}")
	public void updateTask(@PathVariable Long userId, @PathVariable Long id, @RequestBody Task task) {
		taskService.updateTask(userId, id, task);
	}

	@DeleteMapping("/{id}")
	public void deleteTask(@PathVariable Long userId, @PathVariable Long id) {
		taskService.deleteTask(userId, id);
	}

	@GetMapping
	public Tasks listAllTasks(@PathVariable Long userId) {
		return new Tasks(taskService.listAll(userId));
	}

	@GetMapping("/{taskId}")
	public Task getTaskInfo(@PathVariable Long userId, @PathVariable Long taskId) {
		return taskService.getTask(userId, taskId);
	}
}
