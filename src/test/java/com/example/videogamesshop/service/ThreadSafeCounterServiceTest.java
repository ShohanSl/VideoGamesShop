package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.videogamesshop.service.ThreadSafeCounterService.CounterScenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadSafeCounterServiceTest {

    private ThreadSafeCounterService firstService;
    private ThreadSafeCounterService secondService;

    @BeforeEach
    void setUp() {
        firstService = new ThreadSafeCounterService();
        secondService = new ThreadSafeCounterService();

        firstService.reset(CounterScenario.UNSAFE_SHARED);
        firstService.reset(CounterScenario.VOLATILE_SHARED);
        firstService.reset(CounterScenario.SYNCHRONIZED_SHARED);
        firstService.reset(CounterScenario.ATOMIC_SHARED);
        firstService.reset(CounterScenario.INNER_UNSAFE_SHARED);
        firstService.reset(CounterScenario.INNER_VOLATILE_SHARED);
        firstService.reset(CounterScenario.INNER_SYNCHRONIZED_SHARED);
        firstService.reset(CounterScenario.INNER_ATOMIC_SHARED);
    }

    @Test
    void shouldKeepUnsafeLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.UNSAFE_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.UNSAFE_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.UNSAFE_LOCAL));
    }

    @Test
    void shouldKeepVolatileLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.VOLATILE_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.VOLATILE_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.VOLATILE_LOCAL));
    }

    @Test
    void shouldKeepSynchronizedLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.SYNCHRONIZED_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.SYNCHRONIZED_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.SYNCHRONIZED_LOCAL));
    }

    @Test
    void shouldKeepAtomicLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.ATOMIC_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.ATOMIC_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.ATOMIC_LOCAL));
    }

    @Test
    void shouldKeepInnerUnsafeLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_UNSAFE_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.INNER_UNSAFE_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.INNER_UNSAFE_LOCAL));
    }

    @Test
    void shouldKeepInnerVolatileLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_VOLATILE_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.INNER_VOLATILE_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.INNER_VOLATILE_LOCAL));
    }

    @Test
    void shouldKeepInnerSynchronizedLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_SYNCHRONIZED_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.INNER_SYNCHRONIZED_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.INNER_SYNCHRONIZED_LOCAL));
    }

    @Test
    void shouldKeepInnerAtomicLocalCounterInsideSingleServiceInstance() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_LOCAL));
        assertEquals(2L, firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_LOCAL));
        assertEquals(0L, secondService.getCurrent(CounterScenario.INNER_ATOMIC_LOCAL));
    }

    @Test
    void shouldShareInnerUnsafeStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_UNSAFE_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.INNER_UNSAFE_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.INNER_UNSAFE_SHARED));
    }

    @Test
    void shouldShareInnerVolatileStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_VOLATILE_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.INNER_VOLATILE_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.INNER_VOLATILE_SHARED));
    }

    @Test
    void shouldShareInnerSynchronizedStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_SYNCHRONIZED_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.INNER_SYNCHRONIZED_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.INNER_SYNCHRONIZED_SHARED));
    }

    @Test
    void shouldShareInnerAtomicStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.INNER_ATOMIC_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.INNER_ATOMIC_SHARED));
    }

    @Test
    void shouldShareUnsafeStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.UNSAFE_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.UNSAFE_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.UNSAFE_SHARED));
    }

    @Test
    void shouldShareVolatileStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.VOLATILE_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.VOLATILE_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.VOLATILE_SHARED));
    }

    @Test
    void shouldShareSynchronizedStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.SYNCHRONIZED_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.SYNCHRONIZED_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.SYNCHRONIZED_SHARED));
    }

    @Test
    void shouldShareAtomicStaticCounterBetweenPrototypeInstances() {
        assertEquals(1L, firstService.incrementAndGet(CounterScenario.ATOMIC_SHARED));
        assertEquals(2L, secondService.incrementAndGet(CounterScenario.ATOMIC_SHARED));
        assertEquals(2L, firstService.getCurrent(CounterScenario.ATOMIC_SHARED));
    }

    @Test
    void shouldResetOnlySelectedLocalCounterScenario() {
        firstService.incrementAndGet(CounterScenario.ATOMIC_LOCAL);
        firstService.incrementAndGet(CounterScenario.ATOMIC_LOCAL);

        assertEquals(0L, firstService.reset(CounterScenario.ATOMIC_LOCAL));
        assertEquals(0L, firstService.getCurrent(CounterScenario.ATOMIC_LOCAL));
    }

    @Test
    void shouldResetOnlySelectedInnerLocalCounterScenario() {
        firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_LOCAL);
        firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_LOCAL);

        assertEquals(0L, firstService.reset(CounterScenario.INNER_ATOMIC_LOCAL));
        assertEquals(0L, firstService.getCurrent(CounterScenario.INNER_ATOMIC_LOCAL));
    }

    @Test
    void shouldResetSharedCounterForAllPrototypeInstances() {
        firstService.incrementAndGet(CounterScenario.ATOMIC_SHARED);
        secondService.incrementAndGet(CounterScenario.ATOMIC_SHARED);

        assertEquals(0L, firstService.reset(CounterScenario.ATOMIC_SHARED));
        assertEquals(0L, secondService.getCurrent(CounterScenario.ATOMIC_SHARED));
    }

    @Test
    void shouldResetInnerSharedCounterForAllPrototypeInstances() {
        firstService.incrementAndGet(CounterScenario.INNER_ATOMIC_SHARED);
        secondService.incrementAndGet(CounterScenario.INNER_ATOMIC_SHARED);

        assertEquals(0L, firstService.reset(CounterScenario.INNER_ATOMIC_SHARED));
        assertEquals(0L, secondService.getCurrent(CounterScenario.INNER_ATOMIC_SHARED));
    }
}
