package com.example.videogamesshop.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ThreadSafeCounterService {

    public enum CounterScenario {
        UNSAFE_LOCAL,
        VOLATILE_LOCAL,
        SYNCHRONIZED_LOCAL,
        ATOMIC_LOCAL,
        INNER_UNSAFE_LOCAL,
        INNER_VOLATILE_LOCAL,
        INNER_SYNCHRONIZED_LOCAL,
        INNER_ATOMIC_LOCAL,
        INNER_UNSAFE_SHARED,
        INNER_VOLATILE_SHARED,
        INNER_SYNCHRONIZED_SHARED,
        INNER_ATOMIC_SHARED,
        UNSAFE_SHARED,
        VOLATILE_SHARED,
        SYNCHRONIZED_SHARED,
        ATOMIC_SHARED
    }

    private final UnsafeLocalCounter unsafeLocalCounter = new UnsafeLocalCounter();
    private final VolatileLocalCounter volatileLocalCounter = new VolatileLocalCounter();
    private final SynchronizedLocalCounter synchronizedLocalCounter = new SynchronizedLocalCounter();
    private final AtomicLocalCounter atomicLocalCounter = new AtomicLocalCounter();
    private final InnerUnsafeLocalCounter innerUnsafeLocalCounter = new InnerUnsafeLocalCounter();
    private final InnerVolatileLocalCounter innerVolatileLocalCounter = new InnerVolatileLocalCounter();
    private final InnerSynchronizedLocalCounter innerSynchronizedLocalCounter = new InnerSynchronizedLocalCounter();
    private final InnerAtomicLocalCounter innerAtomicLocalCounter = new InnerAtomicLocalCounter();

    public long incrementAndGet(CounterScenario scenario) {
        return switch (scenario) {
            case UNSAFE_LOCAL -> unsafeLocalCounter.incrementAndGet();
            case VOLATILE_LOCAL -> volatileLocalCounter.incrementAndGet();
            case SYNCHRONIZED_LOCAL -> synchronizedLocalCounter.incrementAndGet();
            case ATOMIC_LOCAL -> atomicLocalCounter.incrementAndGet();
            case INNER_UNSAFE_LOCAL -> innerUnsafeLocalCounter.incrementAndGet();
            case INNER_VOLATILE_LOCAL -> innerVolatileLocalCounter.incrementAndGet();
            case INNER_SYNCHRONIZED_LOCAL -> innerSynchronizedLocalCounter.incrementAndGet();
            case INNER_ATOMIC_LOCAL -> innerAtomicLocalCounter.incrementAndGet();
            case INNER_UNSAFE_SHARED -> InnerUnsafeSharedCounter.incrementAndGet();
            case INNER_VOLATILE_SHARED -> InnerVolatileSharedCounter.incrementAndGet();
            case INNER_SYNCHRONIZED_SHARED -> InnerSynchronizedSharedCounter.incrementAndGet();
            case INNER_ATOMIC_SHARED -> InnerAtomicSharedCounter.incrementAndGet();
            case UNSAFE_SHARED -> UnsafeSharedCounter.incrementAndGet();
            case VOLATILE_SHARED -> VolatileSharedCounter.incrementAndGet();
            case SYNCHRONIZED_SHARED -> SynchronizedSharedCounter.incrementAndGet();
            case ATOMIC_SHARED -> AtomicSharedCounter.incrementAndGet();
        };
    }

    public long getCurrent(CounterScenario scenario) {
        return switch (scenario) {
            case UNSAFE_LOCAL -> unsafeLocalCounter.getValue();
            case VOLATILE_LOCAL -> volatileLocalCounter.getValue();
            case SYNCHRONIZED_LOCAL -> synchronizedLocalCounter.getValue();
            case ATOMIC_LOCAL -> atomicLocalCounter.getValue();
            case INNER_UNSAFE_LOCAL -> innerUnsafeLocalCounter.getValue();
            case INNER_VOLATILE_LOCAL -> innerVolatileLocalCounter.getValue();
            case INNER_SYNCHRONIZED_LOCAL -> innerSynchronizedLocalCounter.getValue();
            case INNER_ATOMIC_LOCAL -> innerAtomicLocalCounter.getValue();
            case INNER_UNSAFE_SHARED -> InnerUnsafeSharedCounter.getValue();
            case INNER_VOLATILE_SHARED -> InnerVolatileSharedCounter.getValue();
            case INNER_SYNCHRONIZED_SHARED -> InnerSynchronizedSharedCounter.getValue();
            case INNER_ATOMIC_SHARED -> InnerAtomicSharedCounter.getValue();
            case UNSAFE_SHARED -> UnsafeSharedCounter.getValue();
            case VOLATILE_SHARED -> VolatileSharedCounter.getValue();
            case SYNCHRONIZED_SHARED -> SynchronizedSharedCounter.getValue();
            case ATOMIC_SHARED -> AtomicSharedCounter.getValue();
        };
    }

    public long reset(CounterScenario scenario) {
        return switch (scenario) {
            case UNSAFE_LOCAL -> unsafeLocalCounter.reset();
            case VOLATILE_LOCAL -> volatileLocalCounter.reset();
            case SYNCHRONIZED_LOCAL -> synchronizedLocalCounter.reset();
            case ATOMIC_LOCAL -> atomicLocalCounter.reset();
            case INNER_UNSAFE_LOCAL -> innerUnsafeLocalCounter.reset();
            case INNER_VOLATILE_LOCAL -> innerVolatileLocalCounter.reset();
            case INNER_SYNCHRONIZED_LOCAL -> innerSynchronizedLocalCounter.reset();
            case INNER_ATOMIC_LOCAL -> innerAtomicLocalCounter.reset();
            case INNER_UNSAFE_SHARED -> InnerUnsafeSharedCounter.reset();
            case INNER_VOLATILE_SHARED -> InnerVolatileSharedCounter.reset();
            case INNER_SYNCHRONIZED_SHARED -> InnerSynchronizedSharedCounter.reset();
            case INNER_ATOMIC_SHARED -> InnerAtomicSharedCounter.reset();
            case UNSAFE_SHARED -> UnsafeSharedCounter.reset();
            case VOLATILE_SHARED -> VolatileSharedCounter.reset();
            case SYNCHRONIZED_SHARED -> SynchronizedSharedCounter.reset();
            case ATOMIC_SHARED -> AtomicSharedCounter.reset();
        };
    }

    private static final class UnsafeLocalCounter {
        private long value;

        private long incrementAndGet() {
            value++;
            return value;
        }

        private long getValue() {
            return value;
        }

        private long reset() {
            value = 0;
            return value;
        }
    }

    private static final class VolatileLocalCounter {
        private volatile long value;

        private long incrementAndGet() {
            value++;
            return value;
        }

        private long getValue() {
            return value;
        }

        private long reset() {
            value = 0;
            return value;
        }
    }

    private static final class SynchronizedLocalCounter {
        private long value;

        private synchronized long incrementAndGet() {
            value++;
            return value;
        }

        private synchronized long getValue() {
            return value;
        }

        private synchronized long reset() {
            value = 0;
            return value;
        }
    }

    private static final class AtomicLocalCounter {
        private final AtomicLong value = new AtomicLong();

        private long incrementAndGet() {
            return value.incrementAndGet();
        }

        private long getValue() {
            return value.get();
        }

        private long reset() {
            value.set(0);
            return value.get();
        }
    }

    private final class InnerUnsafeLocalCounter {
        private long value;

        private long incrementAndGet() {
            value++;
            return value;
        }

        private long getValue() {
            return value;
        }

        private long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerVolatileLocalCounter {
        private volatile long value;

        private long incrementAndGet() {
            value++;
            return value;
        }

        private long getValue() {
            return value;
        }

        private long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerSynchronizedLocalCounter {
        private long value;

        private synchronized long incrementAndGet() {
            value++;
            return value;
        }

        private synchronized long getValue() {
            return value;
        }

        private synchronized long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerAtomicLocalCounter {
        private final AtomicLong value = new AtomicLong();

        private long incrementAndGet() {
            return value.incrementAndGet();
        }

        private long getValue() {
            return value.get();
        }

        private long reset() {
            value.set(0);
            return value.get();
        }
    }

    // In modern Java, even a non-static inner class may declare static members.
    // Those members still belong to the nested class itself, so the state is shared
    // across all outer service instances despite the nested class being non-static.
    private final class InnerUnsafeSharedCounter {
        private static long value;

        private static long incrementAndGet() {
            value++;
            return value;
        }

        private static long getValue() {
            return value;
        }

        private static long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerVolatileSharedCounter {
        private static volatile long value;

        private static long incrementAndGet() {
            value++;
            return value;
        }

        private static long getValue() {
            return value;
        }

        private static long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerSynchronizedSharedCounter {
        private static long value;

        private static synchronized long incrementAndGet() {
            value++;
            return value;
        }

        private static synchronized long getValue() {
            return value;
        }

        private static synchronized long reset() {
            value = 0;
            return value;
        }
    }

    private final class InnerAtomicSharedCounter {
        private static final AtomicLong VALUE = new AtomicLong();

        private static long incrementAndGet() {
            return VALUE.incrementAndGet();
        }

        private static long getValue() {
            return VALUE.get();
        }

        private static long reset() {
            VALUE.set(0);
            return VALUE.get();
        }
    }

    private static final class UnsafeSharedCounter {
        private static long value;

        private static long incrementAndGet() {
            value++;
            return value;
        }

        private static long getValue() {
            return value;
        }

        private static long reset() {
            value = 0;
            return value;
        }
    }

    private static final class VolatileSharedCounter {
        private static volatile long value;

        private static long incrementAndGet() {
            value++;
            return value;
        }

        private static long getValue() {
            return value;
        }

        private static long reset() {
            value = 0;
            return value;
        }
    }

    private static final class SynchronizedSharedCounter {
        private static long value;

        private static synchronized long incrementAndGet() {
            value++;
            return value;
        }

        private static synchronized long getValue() {
            return value;
        }

        private static synchronized long reset() {
            value = 0;
            return value;
        }
    }

    private static final class AtomicSharedCounter {
        private static final AtomicLong VALUE = new AtomicLong();

        private static long incrementAndGet() {
            return VALUE.incrementAndGet();
        }

        private static long getValue() {
            return VALUE.get();
        }

        private static long reset() {
            VALUE.set(0);
            return VALUE.get();
        }
    }
}
