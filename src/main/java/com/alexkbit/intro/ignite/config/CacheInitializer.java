package com.alexkbit.intro.ignite.config;

import com.alexkbit.intro.ignite.model.Task;
import lombok.Getter;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

@Getter
public enum CacheInitializer {

    TASK(String.class, Task.class);

    CacheInitializer(Class<?> id, Class<?> model) {
        this.id = id;
        this.model = model;
        this.cacheName = model.getSimpleName() + "Cache";
    }

    private Class<?> id;
    private Class<?> model;
    private String cacheName;

    public static void initCaches(IgniteConfiguration configuration) {
        for(CacheInitializer cache : values()) {
            CacheConfiguration cfg = new CacheConfiguration(cache.cacheName);
            cfg.setIndexedTypes(cache.id, cache.model);
            configuration.setCacheConfiguration(cfg);
        }
    }
}
