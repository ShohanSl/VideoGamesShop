package com.example.videogamesshop.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ThreadSafeCounterService {

    private final AtomicLong counter = new AtomicLong();

    public long incrementAndGet() {
        return counter.incrementAndGet();
    }

    public long getCurrent() {
        return counter.get();
    }

    public long reset() {
        counter.set(0);
        return counter.get();
    }
}
