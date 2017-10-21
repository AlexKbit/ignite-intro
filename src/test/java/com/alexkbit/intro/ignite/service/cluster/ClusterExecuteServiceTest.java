package com.alexkbit.intro.ignite.service.cluster;

import com.alexkbit.intro.ignite.ClusterTest;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.service.TaskService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link ClusterExecuteService}.
 */
@ClusterTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClusterExecuteServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    @Ignore
    public void shouldExecute() {
        Task task = taskService.start("2+5");
        task = taskService.get(task.getId());
        assertEquals(TaskStatus.SUCCESS, task.getStatus());
        assertEquals("7", task.getResult());
    }

    @Test
    @Ignore
    public void shouldExecuteWithError() {
        Task task = taskService.start("2++5");
        task = taskService.get(task.getId());
        assertEquals(TaskStatus.FAIL, task.getStatus());
        assertNotNull(task.getErrorMsg());
    }
}