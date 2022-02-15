package com.blackswan.assesment.task.controller;

import com.blackswan.assesment.task.api.Task;
import com.blackswan.assesment.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerIT {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService mockTaskService;

	@Test
	void canCreateTask() throws Exception {
		// given
		// when
		Task task = new Task("userName", "description", "2016-05-25 14:25:00");
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/user/1/task")
						.content(asJsonString(task))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockTaskService).createTask(1L, task);
	}

	@Test
	void canUpdateTask() throws Exception {
		// when
		Task task = new Task("userName", "description", "2016-05-25 14:25:00");
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/user/2/task/1")
						.content(asJsonString(task))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockTaskService).updateTask(2L,1L, task);
	}

	@Test
	void canDeleteTask() throws Exception {
		// when
		Task task = new Task("userName", "description", "2016-05-25 14:25:00");
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/user/2/task/1")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockTaskService).deleteTask(2L,1L);
	}

	@Test
	void canListAllTasks() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/1/task")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockTaskService).listAll(1L);
	}

	@Test
	void canGetTaskInfo() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/2/task/1")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockTaskService).getTask(2L,1L);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
