package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Job;
import com.alexkbit.intro.ignite.model.Task;
import org.apache.ignite.IgniteQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link TaskService}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskServiceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IgniteQueue<Job> jobQueue;

    @Test
    public void shouldWork() throws InterruptedException {
        int queueSize = jobQueue.size();
        Task task = taskService.start("2+5");
        assertNotNull(task.getId());
        task = taskService.get(task.getId());
        assertNotNull(task);
        assertEquals(queueSize + 1, jobQueue.size());
    }

}