package com.alexkbit.intro.ignite.repository;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import java.util.List;

/**
 * Ignite repository for {@link Task}.
 */
@RepositoryConfig(cacheName = "TaskCache")
public interface TaskRepository extends IgniteRepository<Task, String> {

    List<Task> findByStatus(TaskStatus status);
}
