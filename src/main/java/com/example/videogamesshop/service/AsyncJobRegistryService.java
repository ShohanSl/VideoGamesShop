package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.async.AsyncJobStatusResponse;
import com.example.videogamesshop.exception.AsyncJobNotFoundException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AsyncJobRegistryService {

    private final ConcurrentHashMap<String, AsyncJobState> jobs = new ConcurrentHashMap<>();

    public AsyncJobState createJob() {
        String taskId = UUID.randomUUID().toString();
        AsyncJobState state = new AsyncJobState(taskId);
        jobs.put(taskId, state);
        return state;
    }

    public AsyncJobState getJobState(String taskId) {
        AsyncJobState state = jobs.get(taskId);
        if (state == null) {
            throw new AsyncJobNotFoundException(taskId);
        }
        return state;
    }

    public AsyncJobStatusResponse getJobStatus(String taskId) {
        AsyncJobState state = getJobState(taskId);
        return new AsyncJobStatusResponse(
                state.getTaskId(),
                state.getStatus(),
                state.getCreatedAt(),
                state.getStartedAt(),
                state.getFinishedAt(),
                state.getResult(),
                state.getError()
        );
    }
}
