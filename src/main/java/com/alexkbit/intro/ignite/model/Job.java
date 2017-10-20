package com.alexkbit.intro.ignite.model;

import com.alexkbit.intro.ignite.service.cluster.TaskCacheStore;
import lombok.Data;
import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.UUID;

/**
 * Job for execute.
 */
@Data
public class Job implements IgniteCallable<String> {

    private static final String SCRIPT_ENGINE = "JavaScript";

    @IgniteInstanceResource
    private transient Ignite ignite;
    private transient TaskCacheStore cacheStore;

    private String jobId;
    private String taskId;
    private String expression;

    public Job(String taskId, String expression) {
        this.jobId = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.expression = expression;
    }

    @Override
    public String call() {
        cacheStore = new TaskCacheStore(ignite);
        cacheStore.startProgress(taskId);
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName(SCRIPT_ENGINE);
            String result = engine.eval(expression).toString();
            cacheStore.saveSuccess(taskId, result);
            return result;
        } catch (ScriptException se) {
            cacheStore.saveError(taskId, se.getMessage());
        }
        return null;
    }

    public static Job build(Task task) {
        return new Job(task.getId(), task.getExpression());
    }
}
