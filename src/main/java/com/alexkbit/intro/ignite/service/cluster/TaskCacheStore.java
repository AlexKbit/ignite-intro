package com.alexkbit.intro.ignite.service.cluster;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.io.Serializable;

/**
 * Cache store service for tasks.
 */
public class TaskCacheStore implements Serializable {

    public static final String TASK_CACHE = "TaskCache";

    private String igniteNodeId;
    private transient IgniteCache<String, Task> taskCache;

    public TaskCacheStore(Ignite ignite) {
        igniteNodeId =  ignite.cluster().localNode().id().toString();
        taskCache = ignite.cache(TASK_CACHE);
    }

    public void startProgress(String taskId) {
        Task task = taskCache.get(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setNodeId(igniteNodeId);
        taskCache.put(taskId, task);
    }

    public void saveError(String taskId, String errMsg) {
        Task task = taskCache.get(taskId);
        task.setStatus(TaskStatus.FAIL);
        task.setErrorMsg(errMsg);
        taskCache.put(taskId, task);
    }

    public void saveSuccess(String taskId, String result) {
        Task task = taskCache.get(taskId);
        task.setStatus(TaskStatus.SUCCESS);
        task.setResult(result);
        taskCache.put(taskId, task);
    }


}
