package com.example.videogamesshop.exception;

public class AsyncJobNotFoundException extends ResourceNotFoundException {

    public AsyncJobNotFoundException(String taskId) {
        super("Async job", "taskId", taskId);
    }
}
