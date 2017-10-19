package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link TaskService}.
 */
@IntegrationTest
@RunWith(SpringRunner.class)
public class TaskServiceTest {


    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskService taskService;

    @Test
    public void shouldWork() {
        Task task = taskService.start("2+5");
        assertNotNull(task.getId());
        task = taskService.get(task.getId());
        assertNotNull(task);
        System.out.println(taskService.findByStatus(TaskStatus.WAIT));
    }

}