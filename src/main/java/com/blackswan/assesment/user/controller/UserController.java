package com.blackswan.assesment.user.controller;

import org.springframework.web.bind.annotation.*;

import com.blackswan.assesment.user.api.User;
import com.blackswan.assesment.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping
	public Long createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@PutMapping("/{userId}")
	public String updateUser(@PathVariable Long userId, @RequestBody User user) {
		userService.updateUser(userId, user);
		return "ok";
	}

	@GetMapping
	public List<User> listAllUsers() {
		return userService.listAll();
	}

	@GetMapping("/{userId}")
	public User getUserInfo(@PathVariable Long userId) {
		return userService.getUser(userId);
	}
}
