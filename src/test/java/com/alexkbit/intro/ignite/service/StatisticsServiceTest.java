package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.apache.ignite.Ignite;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link StatisticsService}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsServiceTest {

    private static final String EXPRESSION = "2+5";

    @Autowired
    private Ignite ignite;

    @Autowired
    private TransactionTemplate tx;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StatisticsService statisticsService;

    private String localNodeId;

    @Before
    public void init() {
        localNodeId = ignite.cluster().localNode().id().toString();
    }

    @Test
    @Ignore
    public void shouldGetNodeStat() {
        int oldCount = statisticsService.getNodeStat().get(localNodeId);
        taskService.start(EXPRESSION);
        int newCount = statisticsService.getNodeStat().get(localNodeId);
        assertEquals(oldCount + 1, newCount);
    }

    @Test
    public void shouldGetStatusStat() {
        int oldCount = statisticsService.getStatusStat().get(TaskStatus.WAIT);
        taskService.start(EXPRESSION);
        int newCount = statisticsService.getStatusStat().get(TaskStatus.WAIT);;
        assertEquals(oldCount + 1, newCount);
    }
}