package com.alexkbit.intro.ignite.model;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Task.
 */
@Data
public class Task implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField
    private String expression;

    @QuerySqlField
    private String result;

    @QuerySqlField(index = true)
    private TaskStatus status;

    @QuerySqlField
    private String errorMsg;
}
