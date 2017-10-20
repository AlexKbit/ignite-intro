package com.alexkbit.intro.ignite.model;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;

/**
 * Task.
 */
@Data
public class Task implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String nodeId;

    @QuerySqlField(index = true)
    private Date createdAt;

    @QuerySqlField
    private String expression;

    @QuerySqlField
    private String result;

    @QuerySqlField(index = true)
    private TaskStatus status;

    @QuerySqlField
    private String errorMsg;

    public Task() {
        this.createdAt = new Date();
    }
}
