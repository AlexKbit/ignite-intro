package com.alexkbit.intro.ignite.service.cluster;

import org.apache.ignite.services.Service;

/**
 * Iterface for ignite service.
 */
public interface ClusterExecuteService extends Service {
    void execute();
}
