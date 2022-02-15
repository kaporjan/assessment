package com.blackswan.assesment.task.service;

import com.blackswan.assesment.task.api.Task;
import com.blackswan.assesment.task.repository.TaskRepository;
import com.blackswan.assesment.user.entity.UserEntity;
import com.blackswan.assesment.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        super();
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Long createTask(Long userId, Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Optional<UserEntity> user = userRepository.findById(userId);
        Long id = null;
        if (user.isPresent()) {
            com.blackswan.assesment.task.entity.Task saved = taskRepository.save(
                    new com.blackswan.assesment.task.entity.Task(
                            user.get(),
                            task.getName(),
                            task.getDescription(),
                            LocalDateTime.parse(task.getDate_time(), formatter)));
            id = Optional.ofNullable(saved).map(o -> o.getId()).orElse(null);
        }
        return id;
    }

    @Transactional
    public void updateTask(Long userId, Long id, Task taskRequest) {
        Optional<UserEntity> user = userRepository.findById(userId);
        user.ifPresent(u -> {
                    Optional<com.blackswan.assesment.task.entity.Task> optionalTask = taskRepository.findById(id);
                    if (optionalTask.isPresent()) {
                        com.blackswan.assesment.task.entity.Task task = optionalTask.get();
                        task.setName(taskRequest.getName());
                        task.setDescription(taskRequest.getDescription());
                        task.setDate_time(LocalDateTime.parse(taskRequest.getDate_time()));
                        taskRepository.save(task);
                    }
                }
        );
    }

    public List<Task> listAll(Long userId) {
        List<Task> result = new ArrayList<>();
        for (com.blackswan.assesment.task.entity.Task task : taskRepository.findByUserId(userId)) {
            result.add(new Task(task.getName(), task.getDescription(), task.getDate_time().toString()));
        }
        return result;
    }

    public Task getTask(Long userId, Long id) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<com.blackswan.assesment.task.entity.Task> task = Optional.empty();
        if (user.isPresent()) {
            task = taskRepository.findByIdAndUserId(id, user.get().getId()).stream().findFirst();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return task.map(u -> new Task(u.getName(), u.getDescription(), u.getDate_time().format(formatter)))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteTask(Long userId, Long id) {
        taskRepository.deleteByIdAndUserId(id, userId);
    }
}
