package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.async.AsyncJobStatus;
import lombok.Getter;

import java.time.OffsetDateTime;

public class AsyncJobState {

    private final String taskId;
    private final OffsetDateTime createdAt;
    private volatile AsyncJobStatus status;
    private volatile OffsetDateTime startedAt;
    private volatile OffsetDateTime finishedAt;
    private volatile String result;
    private volatile String error;

    public AsyncJobState(String taskId) {
        this.taskId = taskId;
        this.createdAt = OffsetDateTime.now();
        this.status = AsyncJobStatus.PENDING;
    }

    public synchronized void markRunning() {
        status = AsyncJobStatus.RUNNING;
        startedAt = OffsetDateTime.now();
    }

    public synchronized void markCompleted(String jobResult) {
        status = AsyncJobStatus.COMPLETED;
        finishedAt = OffsetDateTime.now();
        result = jobResult;
        error = null;
    }

    public synchronized void markFailed(String jobError) {
        status = AsyncJobStatus.FAILED;
        finishedAt = OffsetDateTime.now();
        error = jobError;
    }

    public String getTaskId() {
        return taskId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public AsyncJobStatus getStatus() {
        return status;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
