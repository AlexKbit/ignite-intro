package com.alexkbit.intro.ignite.config;

import com.alexkbit.intro.ignite.service.cluster.ClusterExecuteService;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Ignite service configuration.
 */
@Configuration
@Profile("!test")
public class ServiceConfig {

    @Autowired
    private Ignite ignite;

    @PostConstruct
    public void init() {
        IgniteServices services = ignite.services();
        if (services.service(ClusterExecuteService.SERVICE_NAME) == null) {
            services.deployNodeSingleton(ClusterExecuteService.SERVICE_NAME, new ClusterExecuteService());
        }
    }
}
