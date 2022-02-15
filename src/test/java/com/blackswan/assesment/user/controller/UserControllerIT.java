package com.blackswan.assesment.user.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackswan.assesment.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.blackswan.assesment.user.api.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerIT {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService mockUserService;
	private User user = new User("userName", "firstName", "lastName");

	@Test
	void canCreateUser() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/user")
						.content(asJsonString(user))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockUserService).createUser(user);
	}

	@Test
	void canUpdateUser() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/user/1")
						.content(asJsonString(user))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockUserService).updateUser(1L, user);
	}

	@Test
	void canListAllUsers() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockUserService).listAll();
	}

	@Test
	void canGetUserInfo() throws Exception {
		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/1")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		verify(mockUserService).getUser(1L);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
