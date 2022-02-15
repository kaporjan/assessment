package com.blackswan.assesment.user.controller;

import com.blackswan.assesment.user.api.User;
import com.blackswan.assesment.user.entity.UserEntity;
import com.blackswan.assesment.user.repository.UserRepository;
import com.blackswan.assesment.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.hasText;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService mockUserService;
	private User user = new User("userName", "firstName", "lastName");
	private User user2 = new User("newName", "firstName", "lastName");
	private ObjectMapper OM = new ObjectMapper();

	@Test
	void canCreateUser() throws Exception {
		// given
		Long uid = createUser();

		// when
		Optional<UserEntity> actual = userRepository.findById(uid);

		// then
		hasText(actual.get().getUserName(),"userName");
	}

	@Test
	void canUpdateUser() throws Exception {
		// given
		Long uid = createUser();

		// when
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/user/"+uid)
						.content(asJsonString(user2))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// then
		Optional<UserEntity> actual = userRepository.findById(uid);
		hasText(actual.get().getUserName(),"newName");
	}

	@Test
	void canListAllUsers() throws Exception {
		// given
		createUser();
		createUser();
		createUser();

		// when
		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		List actual = OM.readValue(result.getResponse().getContentAsString(), List.class);

		// then
		assertEquals(3, actual.size());
	}

	@Test
	void canGetUserInfo() throws Exception {
		Long uid = createUser();
		// when

		User actual = OM.readValue(this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/user/"+uid)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString(), User.class);

		// then
		assertEquals(user, actual);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
}
