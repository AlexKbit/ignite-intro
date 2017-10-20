package com.alexkbit.intro.ignite.config;

import com.alexkbit.intro.ignite.service.cluster.ClusterExecuteService;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Configuration for ignite services.
 */
@Profile("!test")
@Configuration
public class IgniteServiceConfig {

    @Autowired
    private Ignite ignite;

    @PostConstruct
    public void init() {
        ignite.services().deployClusterSingleton(ClusterExecuteService.SERVICE_NAME, new ClusterExecuteService());
    }
}
