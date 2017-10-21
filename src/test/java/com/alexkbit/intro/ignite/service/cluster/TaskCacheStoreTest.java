package com.alexkbit.intro.ignite.service.cluster;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.service.TaskService;
import org.apache.ignite.Ignite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link TaskCacheStore}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskCacheStoreTest {

    @Autowired
    private Ignite ignite;

    @Autowired
    private TaskService taskService;

    private TaskCacheStore cacheStore;
    private String taskId;

    @Before
    public void init() {
        cacheStore = new TaskCacheStore(ignite);
        taskId = taskService.start("0").getId();
    }

    @Test
    public void shouldSaveInProgress() {
        cacheStore.startProgress(taskId);
        assertEquals(TaskStatus.IN_PROGRESS, taskService.get(taskId).getStatus());
    }

    @Test
    public void shouldSaveSuccess() {
        String result = UUID.randomUUID().toString();
        cacheStore.saveSuccess(taskId, result);
        Task task = taskService.get(taskId);
        assertEquals(TaskStatus.SUCCESS, task.getStatus());
        assertEquals(result, task.getResult());
    }

    @Test
    public void shouldSaveFail() {
        String errMsg = UUID.randomUUID().toString();
        cacheStore.saveError(taskId, errMsg);
        Task task = taskService.get(taskId);
        assertEquals(TaskStatus.FAIL, task.getStatus());
        assertEquals(errMsg, task.getErrorMsg());
    }
}