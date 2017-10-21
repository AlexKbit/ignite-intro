package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.model.Job;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.repository.TaskRepository;
import groovy.util.logging.Slf4j;
import org.apache.ignite.IgniteQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
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
    private IgniteQueue<Job> jobQueue;

    public Task start(String expression) {
        Task task = new Task();
        task.setStatus(TaskStatus.WAIT);
        task.setExpression(expression);
        task = taskRepository.save(task.getId(), task);
        jobQueue.add(Job.build(task));
        return task;
    }

    public Task get(String taskId) {
        return taskRepository.findOne(taskId);
    }

    public boolean stop(String taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task == null || !jobQueue.removeIf(job -> job.getTaskId().equalsIgnoreCase(taskId))) {
            return false;
        }
        task.setStatus(TaskStatus.STOPPED);
        taskRepository.save(task.getId(), task);
        return true;
    }
    public void remove(String taskId) {
        taskRepository.delete(taskId);
    }

    public Page<Task> load(int page, int count) {
        Pageable pageable = new PageRequest(page, count);
        Iterator<Task> iterator = taskRepository.findAll().iterator();
        int skip = page * count;
        while (skip-- > 0 && iterator.hasNext()) {
            iterator.next();
        }
        List<Task> tasks = new ArrayList<>(count);
        for (int i = 0; i < count && iterator.hasNext(); i++) {
            tasks.add(iterator.next());
        }
        return new PageImpl<>(tasks, pageable, taskRepository.count());
    }
}
