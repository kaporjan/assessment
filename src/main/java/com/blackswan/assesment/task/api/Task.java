package com.blackswan.assesment.task.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class Task {
	private final String name;
	private final String description;
	private final String date_time;

	@JsonCreator
	public Task(@JsonProperty("name") String name,
				@JsonProperty("description") String description,
				@JsonProperty("date_time") String date_time) {
		this.name = name;
		this.description = description;
		this.date_time = date_time;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDate_time() {
		return date_time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(date_time, task.date_time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, date_time);
	}

	@Override
	public String toString() {
		return "Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", date_time='" + date_time + '\'' +
				'}';
	}
}
