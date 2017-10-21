package com.alexkbit.intro.ignite.repository;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.Query;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Ignite repository for {@link Task}.
 */
@RepositoryConfig(cacheName = "TaskCache")
public interface TaskRepository extends IgniteRepository<Task, String> {

    @Query("SELECT * FROM Task order by createdAt desc")
    List<Task> findAllOrderByCreatedAt(Pageable pageable);

    int countByStatus(TaskStatus status);

    int countByNodeId(String nodeId);
}
