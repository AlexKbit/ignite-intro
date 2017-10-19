package com.alexkbit.intro.ignite.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.PersistentStoreConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Ignite configuration.
 *
 * @author Aleksandr_Savchenko1
 */
@Configuration
@EnableIgniteRepositories("com.alexkbit.intro.ignite.repository")
public class IgniteConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${app.ignite.persistent.active}")
    private boolean persistentActive;

    @Value("${app.ignite.persistent.path}")
    private String persistentPath;

    @Value("#{'${app.ignite.cluster.addresses}'.split(',')}")
    private List<String> clusterAdresses;

    @Bean
    public TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder() {
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder(true);
        tcpDiscoveryVmIpFinder.setAddresses(clusterAdresses);
        return tcpDiscoveryVmIpFinder;
    }

    @Bean
    public TcpDiscoverySpi tcpDiscoverySpi(TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder) {
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);
        return discoverySpi;
    }

    @Bean
    public IgniteConfiguration igniteCfg(TcpDiscoverySpi tcpDiscoverySpi) {
        IgniteConfiguration icfg = new IgniteConfiguration();
        icfg.setPeerClassLoadingEnabled(true);
        CacheInitializer.initCaches(icfg);

        /*ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
        serviceConfiguration.setService(new ClusterExecuteService());
        serviceConfiguration.setName(ClusterExecuteService.SERVICE_NAME);
        icfg.setServiceConfiguration(serviceConfiguration);*/

        if (persistentActive) {
            icfg.setPersistentStoreConfiguration(persistentStoreConfiguration());
        }
        icfg.setDiscoverySpi(tcpDiscoverySpi);
        return icfg;
    }

    @Bean
    public Ignite igniteInstance(IgniteConfiguration igniteConfiguration) throws IgniteCheckedException {
        Ignite ignite = IgniteSpring.start(igniteConfiguration, applicationContext);
        if (!persistentActive) {
            return ignite;
        }
        ignite.active(true);
        return ignite;
    }

    private PersistentStoreConfiguration persistentStoreConfiguration() {
        PersistentStoreConfiguration pscfg = new PersistentStoreConfiguration();
        pscfg.setPersistentStorePath(persistentPath);
        return pscfg;
    }
}
