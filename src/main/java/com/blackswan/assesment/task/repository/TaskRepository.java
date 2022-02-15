package com.blackswan.assesment.task.repository;

import com.blackswan.assesment.task.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    public List<Task> findByIdAndUserId(Long id, Long userId);

    public List<Task> deleteByIdAndUserId(Long id, Long userId);

    public List<Task> findByUserId(Long userId);
}
