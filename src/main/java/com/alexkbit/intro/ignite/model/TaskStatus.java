package com.alexkbit.intro.ignite.model;

/**
 * Task Statuses.
 */
public enum TaskStatus {
    /**
     * Task wait a manager for work
     */
    WAIT,
    /**
     * Task in progress
     */
    IN_PROGRESS,
    /**
     * Task is done with result
     */
    SUCCESS,
    /**
     * Task failed and should exist error message
     */
    FAIL,
    /**
     * Task was stopped
     */
    STOPPED
}

