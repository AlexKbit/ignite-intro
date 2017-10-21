package com.alexkbit.intro.ignite.service;

import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.repository.TaskRepository;
import groovy.util.logging.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for get statistics about tasks.
 */
@Slf4j
@Service
public class StatisticsService {

    @Autowired
    private Ignite ignite;

    @Autowired
    private TaskRepository taskRepository;

    public Map<String, Integer> getNodeStat() {
        Map<String, Integer> stat = new HashMap<>();
        for (ClusterNode node : ignite.cluster().nodes()) {
            String nodeId = node.id().toString();
            int count = taskRepository.countByNodeId(nodeId);
            stat.put(nodeId, count);
        }
        return stat;
    }

    public Map<TaskStatus, Integer> getStatusStat() {
        Map<TaskStatus, Integer> stat = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            stat.put(status, taskRepository.countByStatus(status));
        }
        return stat;
    }
}
