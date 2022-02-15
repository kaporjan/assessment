package com.blackswan.assesment.user.service;

import com.blackswan.assesment.user.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blackswan.assesment.user.api.User;
import com.blackswan.assesment.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private final UserRepository repository;

	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public Long createUser(User user) {
		Long id = null;
		UserEntity saved = repository.save(new UserEntity(user.getUserName(), user.getFirstName(), user.getLastName()));
		if (saved != null) id = saved.getId();
		return id;
	}

	@Transactional
	public void updateUser(Long id, User userRequest) {
		Optional<UserEntity> optionalUser = repository.findById(id);
		if (optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();
			user.setUserName(userRequest.getUserName());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			repository.save(user);
		}
	}

	public List<User> listAll() {
		List<User> result = new ArrayList<>();
		for (UserEntity user : repository.findAll()) {
			result.add(new User(user.getUserName(), user.getFirstName(), user.getLastName()));
		}
		return result;
	}

	public User getUser(Long id) {
		Optional<UserEntity> user = repository.findById(id);
        return user.map(u -> new User(u.getUserName(),u.getFirstName(),u.getLastName()))
		.orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
	}
}
