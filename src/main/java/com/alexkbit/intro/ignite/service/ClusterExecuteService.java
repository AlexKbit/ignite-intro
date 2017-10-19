package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.model.TaskStatus;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Cluster service for execute tasks.
 */
public class ClusterExecuteService implements Service {

    public static final String SCRIPT_ENGINE = "JavaScript";
    public static final String TASK_QUEUE = "TASK_QUEUE";
    public static final String TASK_CACHE = "TaskCache";
    public static final String SERVICE_NAME = "clusterExecuteService";

    @IgniteInstanceResource
    private transient Ignite ignite;
    private transient IgniteQueue<String> taskQueue;
    private transient IgniteCache<String, Task> taskCache;

    @Override
    public void cancel(ServiceContext ctx) {
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        taskQueue = ignite.queue(ClusterExecuteService.TASK_QUEUE, 0, new CollectionConfiguration());
        taskCache = ignite.cache(TASK_CACHE);
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        while (!ctx.isCancelled()) {
            if (freeThreadExists() && !taskQueue.isEmpty()) {
                execute(taskCache.get(taskQueue.poll()));
            }
        }
    }

    private void execute(Task task) {
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskCache.put(task.getId(), task);
        try {
            String result = ignite.compute().apply(expressionSolver(), task.getExpression());
            task.setStatus(TaskStatus.SUCCESS);
            task.setResult(result);
        } catch (Exception e) {
            task.setStatus(TaskStatus.FAIL);
            task.setErrorMsg(e.getMessage());
        }
        taskCache.put(task.getId(), task);
    }

    private IgniteClosure<String, String> expressionSolver() {
        return (IgniteClosure<String, String>) s -> {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName(SCRIPT_ENGINE);
            try {
                return engine.eval(s).toString();
            } catch (ScriptException se) {
                throw new IllegalArgumentException(se);
            }
        };
    }

    private boolean freeThreadExists() {
        int computePoolSize = ignite.configuration().getPublicThreadPoolSize();
        int currentActiveTasksInCluster = 0;
        for (ClusterNode clusterNode : ignite.cluster().topology(ignite.cluster().topologyVersion())) {
            ClusterMetrics clusterMetrics = clusterNode.metrics();
            currentActiveTasksInCluster += clusterMetrics.getCurrentActiveJobs();
        }
        return computePoolSize - currentActiveTasksInCluster > 0;
    }
}
