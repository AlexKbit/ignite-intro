package com.alexkbit.intro.ignite.service.cluster;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Job;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.service.TaskService;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.services.ServiceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ClusterExecuteService}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClusterExecuteServiceTest {

    @Autowired
    private Ignite ignite;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IgniteQueue<Job> jobQueue;

    private ClusterExecuteService executeService;

    @Mock
    private ServiceContext ctx;

    @Before
    public void init() throws Exception {
        executeService = new ClusterExecuteService(ignite);
        executeService.init(ctx);
        jobQueue.clear();
    }

    @After
    public void after() {
        executeService.cancel(ctx);
    }

    @Test
    public void shouldExecute() throws Exception {
        when(ctx.isCancelled()).thenReturn(false);
        Task task = taskService.start("2+5");
        executeService.execute().get();
        task = taskService.get(task.getId());
        assertEquals(TaskStatus.SUCCESS, task.getStatus());
        assertEquals("7", task.getResult());
    }

    @Test
    public void shouldExecuteWithError() throws Exception {
        when(ctx.isCancelled()).thenReturn(false);
        Task task = taskService.start("2++5");
        executeService.execute().get();
        task = taskService.get(task.getId());
        assertEquals(TaskStatus.FAIL, task.getStatus());
        assertNotNull(task.getErrorMsg());
    }

    @Test
    public void shouldNotExecute() throws Exception {
        when(ctx.isCancelled()).thenReturn(true);
        Task task = taskService.start("1");
        executeService.execute(ctx);
        task = taskService.get(task.getId());
        assertEquals(TaskStatus.WAIT, task.getStatus());
    }
}