package com.blackswan.assesment.task.controller;

import com.blackswan.assesment.task.api.Task;
import com.blackswan.assesment.task.api.Tasks;
import com.blackswan.assesment.task.repository.TaskRepository;
import com.blackswan.assesment.user.api.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIT {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private TaskRepository taskRepository;
	private static final ObjectMapper OM = new ObjectMapper();
	private Task task = new Task("taskName", "description", "2016-05-25 14:25:00");
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Test
	void canCreateTask() throws Exception {
		// given
		Long userId = createUser();
		// when
		Long taskId = createTaskFor(userId);

		// then
		Optional<com.blackswan.assesment.task.entity.Task> actual = taskRepository.findById(taskId);

		assertEquals(actual.get().getName(),task.getName());
		assertEquals(actual.get().getDescription(),task.getDescription());
		assertEquals(actual.get().getDate_time().format(formatter),
				task.getDate_time());
	}

	@Test
	void canUpdateTask() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/user/2/task/1")
						.content(asJsonString(task))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
	}

	@Test
	void canDeleteTask() throws Exception {
		// given
		Long userId = createUser();
		Long taskId = createTaskFor(userId);

		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/user/"+userId+"/task/"+taskId)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		Optional<com.blackswan.assesment.task.entity.Task> actual = taskRepository.findById(taskId);
		assertFalse(actual.isPresent());
	}

	@Test
	void canListAllTasks() throws Exception {
		Long uid = createUser();
        createTaskFor(uid);
		createTaskFor(uid);
		// when
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/"+uid+"/task")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		// then
		Tasks tasks = OM.readValue(result.getResponse().getContentAsString(), Tasks.class);
		Assertions.assertTrue(tasks.getTasks().size() == 2);
	}

	@Test
	void canGetTaskInfo() throws Exception {
		Long uid = createUser();
		Long id = createTaskFor(uid);
		// when
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/"+uid+"/task/"+id)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		// then
		Task actualTask = OM.readValue(result.getResponse().getContentAsString(), Task.class);
		assertEquals(actualTask, task);
	}

	private Long createUser() throws Exception {
		return OM.readValue(this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/user")
						.content(asJsonString(
								new User("userName", "firstName", "lastName")))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), Long.class);
	}

	private Long createTaskFor(Long userId) throws Exception {
		return OM.readValue(this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/user/"+userId+"/task")
						.content(asJsonString(task))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), Long.class);
	}

	public static String asJsonString(final Object obj) {
		try {
			return OM.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
