package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Job;
import com.alexkbit.intro.ignite.model.Task;
import org.apache.ignite.IgniteQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Test for {@link TaskService}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskServiceTest {

    private static final String EXPRESSION = "2+5";

    @Autowired
    private TaskService taskService;

    @Autowired
    private IgniteQueue<Job> jobQueue;

    @Before
    public void init() {
        jobQueue.clear();
    }

    @Test
    public void shouldStart() {
        int queueSize = jobQueue.size();
        Task task = taskService.start(EXPRESSION);
        assertNotNull(task.getId());
        task = taskService.get(task.getId());
        assertNotNull(task);
        assertEquals(queueSize + 1, jobQueue.size());
    }

    @Test
    public void shouldStop() {
        Task task = taskService.start(EXPRESSION);
        int queueSize = jobQueue.size();
        boolean result = taskService.stop(task.getId());
        assertTrue(result);
        assertEquals(queueSize - 1, jobQueue.size());
    }

    @Test
    public void shouldNotStop() {
        Task task = taskService.start(EXPRESSION);
        jobQueue.poll();
        int queueSize = jobQueue.size();
        boolean result = taskService.stop(task.getId());
        assertFalse(result);
        assertEquals(queueSize, jobQueue.size());
    }

    @Test
    public void shouldLoadAll() {
        taskService.start(EXPRESSION);
        Page page = taskService.load(0, 1);
        assertEquals(1, page.getSize());
        assertEquals(0, page.getNumber());
    }

    @Test
    public void shouldLoadSecondPage() {
        taskService.start(EXPRESSION);
        Page page = taskService.load(1, 1);
        assertEquals(1, page.getNumber());
    }

}