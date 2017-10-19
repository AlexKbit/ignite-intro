package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.repository.TaskRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * Cluster service for execute tasks.
 */
public class ClusterExecuteService implements Service {

    public static final String SERVICE_NAME = "clusterExecuteService";

    @IgniteInstanceResource
    private transient Ignite ignite;

    @SpringResource
    private transient TaskRepository taskRepository;

    @Override
    public void cancel(ServiceContext ctx) {

    }

    @Override
    public void init(ServiceContext ctx) throws Exception {

    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        while (!ctx.isCancelled()) {
            int freeThreads = getFreeThreads();
            executeNext(freeThreads);
        }
    }

    private void executeNext(int freeThreads) {
        List<Task> taskList = taskRepository.findByStatus(TaskStatus.WAIT);
        taskList.forEach(this::execute);
    }

    private void execute(Task task) {
        try {
            String result = ignite.compute().call(createCallable(task.getExpression()));
            task.setStatus(TaskStatus.SUCCESS);
            task.setResult(result);
        } catch (IllegalArgumentException e) {
            task.setStatus(TaskStatus.FAIL);
            task.setErrorMsg(e.getMessage());
        }
        taskRepository.save(task.getId(), task);

    }

    private IgniteCallable<String> createCallable(String expression) {
        return (IgniteCallable<String>) () -> {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            try {
                return engine.eval(expression).toString();
            } catch (ScriptException se) {
                throw new IllegalArgumentException(se);
            }
        };
    }

    private int getFreeThreads() {
        int computePoolSize = ignite.configuration().getPublicThreadPoolSize();
        int currentActiveTasksInCluster = 0;
        for (ClusterNode clusterNode : ignite.cluster().topology(ignite.cluster().topologyVersion())) {
            ClusterMetrics clusterMetrics = clusterNode.metrics();
            currentActiveTasksInCluster += clusterMetrics.getCurrentActiveJobs();
        }
        return computePoolSize - currentActiveTasksInCluster;
    }
}
