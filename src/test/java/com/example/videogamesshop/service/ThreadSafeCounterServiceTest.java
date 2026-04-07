package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ThreadSafeCounterServiceTest {

    private final ThreadSafeCounterService threadSafeCounterService = new ThreadSafeCounterService();

    @Test
    void shouldIncrementCounter() {
        assertEquals(1L, threadSafeCounterService.incrementAndGet());
        assertEquals(2L, threadSafeCounterService.incrementAndGet());
        assertEquals(2L, threadSafeCounterService.getCurrent());
    }

    @Test
    void shouldResetCounter() {
        threadSafeCounterService.incrementAndGet();
        threadSafeCounterService.incrementAndGet();

        assertEquals(0L, threadSafeCounterService.reset());
        assertEquals(0L, threadSafeCounterService.getCurrent());
    }
}
