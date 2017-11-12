package com.alexkbit.intro.ignite.model;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Task.
 */
@Data
public class Task implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField
    private String nodeId;

    @QuerySqlField(index = true)
    private Date createdAt;

    private String expression;

    private String result;

    @QuerySqlField
    private TaskStatus status;

    private String errorMsg;

    public Task() {
        this.setId(UUID.randomUUID().toString());
        this.createdAt = new Date();
    }
}
