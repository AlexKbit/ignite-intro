package com.alexkbit.intro.ignite.service.cluster;

import com.alexkbit.intro.ignite.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;

/**
 * Cluster service for execute tasks.
 */
@Slf4j
public class ClusterExecuteServiceImpl implements ClusterExecuteService {

    public static final String JOB_QUEUE = "JOB_QUEUE";
    public static final String SERVICE_NAME = "clusterExecuteService";

    @IgniteInstanceResource
    private transient Ignite ignite;
    private transient IgniteQueue<Job> jobQueue;
    private transient TaskCacheStore cacheStore;

    @Override
    public void cancel(ServiceContext ctx) {
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        jobQueue = ignite.queue(ClusterExecuteServiceImpl.JOB_QUEUE, 0, new CollectionConfiguration());
        cacheStore = new TaskCacheStore(ignite);
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        while (!ctx.isCancelled()) {
            execute();
        }
    }

    public void execute() {
        if (freeThreadExists() && !jobQueue.isEmpty()) {
            executeJob(jobQueue.poll());
        }
    }

    private void executeJob(Job job) {
        try {
            log.debug(String.format("Start execution job[%s] for task[%s]", job.getJobId(), job.getTaskId()));
            IgniteFuture<String> future = ignite.compute().callAsync(job);
            future.listen(t -> log.debug(String.format("End execution job[%s] for task[%s]", job.getJobId(), job.getTaskId())));
        } catch (Exception e) {
            cacheStore.saveError(job.getTaskId(), e.getMessage());
        }
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
