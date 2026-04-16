package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.concurrency.RaceConditionResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class RaceConditionDemoService {

    public RaceConditionResponse runUnsafeDemo(int threads, int incrementsPerThread) {
        UnsafeCounter counter = new UnsafeCounter();
        long durationMs = runIncrementScenario(threads, incrementsPerThread, counter::increment);
        long expected = (long) threads * incrementsPerThread;
        long actual = counter.getValue();
        return buildResponse("unsafe", threads, incrementsPerThread, expected, actual, durationMs);
    }

    public RaceConditionResponse runSafeDemo(int threads, int incrementsPerThread) {
        AtomicLong counter = new AtomicLong();
        long durationMs = runIncrementScenario(threads, incrementsPerThread, counter::incrementAndGet);
        long expected = (long) threads * incrementsPerThread;
        long actual = counter.get();
        return buildResponse("safe", threads, incrementsPerThread, expected, actual, durationMs);
    }

    private long runIncrementScenario(int threads, int incrementsPerThread, Runnable incrementAction) {
        long startedAt = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch readyLatch = new CountDownLatch(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            tasks.add(() -> {
                readyLatch.countDown();
                awaitLatch(startLatch);
                for (int j = 0; j < incrementsPerThread; j++) {
                    incrementAction.run();
                }
                doneLatch.countDown();
            });
        }

        tasks.forEach(executorService::submit);
        awaitLatch(readyLatch);
        startLatch.countDown();
        awaitLatch(doneLatch);
        shutdownExecutor(executorService);
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
    }

    private RaceConditionResponse buildResponse(
            String mode,
            int threads,
            int incrementsPerThread,
            long expected,
            long actual,
            long durationMs
    ) {
        return new RaceConditionResponse(
                mode,
                threads,
                incrementsPerThread,
                expected,
                actual,
                expected - actual,
                durationMs
        );
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Concurrent demo was interrupted", exception);
        }
    }

    private void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException exception) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Executor shutdown was interrupted", exception);
        }
    }
    private static final class UnsafeCounter {
        private long value;

        private void increment() {
            value++;
        }

        private long getValue() {
            return value;
        }
    }
}
