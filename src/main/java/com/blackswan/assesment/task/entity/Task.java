package com.blackswan.assesment.task.entity;

import com.blackswan.assesment.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Task implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private UserEntity user;

	private String name;
	private String description;
	private LocalDateTime date_time;

	public Task(UserEntity user, String name, String description, LocalDateTime date_time) {
		this.user = user;
		this.name = name;
		this.description = description;
		this.date_time = date_time;
	}

	public Task() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDate_time() {
		return date_time;
	}

	public void setDate_time(LocalDateTime date_time) {
		this.date_time = date_time;
	}

	@Override
	public String toString() {
		return "Task{" +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", date_time=" + date_time +
				'}';
	}
}
