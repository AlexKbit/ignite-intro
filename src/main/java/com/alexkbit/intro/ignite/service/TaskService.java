package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.repository.TaskRepository;
import groovy.util.logging.Slf4j;
import org.apache.ignite.IgniteQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for {@link Task}.
 */
@Slf4j
@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private IgniteQueue<String> taskQueue;

    public Task start(String expression) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setStatus(TaskStatus.WAIT);
        task.setExpression(expression);
        task = taskRepository.save(task.getId(), task);
        taskQueue.add(task.getId());
        return task;
    }

    public Task get(String taskId) {
        return taskRepository.findOne(taskId);
    }

    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
}
