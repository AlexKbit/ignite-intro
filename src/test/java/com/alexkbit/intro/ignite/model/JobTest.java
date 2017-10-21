package com.alexkbit.intro.ignite.model;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.service.TaskService;
import org.apache.ignite.Ignite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link Job}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JobTest {

    @Autowired
    private Ignite ignite;

    @Autowired
    private TaskService taskService;

    @Test
    public void shouldExecute() {
        Task task = taskService.start("1+1");
        Job job = Job.build(task);
        job.setIgnite(ignite);
        String result = job.call();
        Task changedTask = taskService.get(job.getTaskId());
        assertEquals(TaskStatus.SUCCESS, changedTask.getStatus());
        assertEquals(result, changedTask.getResult());
    }

    @Test
    public void shouldExecuteWithError() {
        Task task = taskService.start("1--1");
        Job job = Job.build(task);
        job.setIgnite(ignite);
        String result = job.call();
        Task changedTask = taskService.get(job.getTaskId());
        assertEquals(TaskStatus.FAIL, changedTask.getStatus());
        assertEquals(result, changedTask.getResult());
        assertNotNull(changedTask.getErrorMsg());
    }

}