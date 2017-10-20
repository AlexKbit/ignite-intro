package com.alexkbit.intro.ignite.model;

import lombok.Data;
import org.apache.ignite.lang.IgniteCallable;

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

    private String jobId;
    private String taskId;
    private String expression;

    public Job(String taskId, String expression) {
        this.jobId = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.expression = expression;
    }

    @Override
    public String call() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(SCRIPT_ENGINE);
        try {
            return engine.eval(expression).toString();
        } catch (ScriptException se) {
            throw new IllegalArgumentException(se);
        }
    }

    public static Job build(Task task) {
        return new Job(task.getId(), task.getExpression());
    }
}
