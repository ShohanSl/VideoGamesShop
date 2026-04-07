package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.videogamesshop.dto.concurrency.RaceConditionResponse;
import org.junit.jupiter.api.Test;

class RaceConditionDemoServiceTest {

    private final RaceConditionDemoService raceConditionDemoService = new RaceConditionDemoService();

    @Test
    void shouldReturnExactValueForSafeDemo() {
        RaceConditionResponse response = raceConditionDemoService.runSafeDemo(50, 1000);

        assertEquals(50_000L, response.expected());
        assertEquals(50_000L, response.actual());
        assertEquals(0L, response.lostUpdates());
    }

    @Test
    void shouldNeverExceedExpectedValueForUnsafeDemo() {
        RaceConditionResponse response = raceConditionDemoService.runUnsafeDemo(50, 1000);

        assertEquals(50_000L, response.expected());
        assertTrue(response.actual() <= response.expected());
        assertTrue(response.lostUpdates() >= 0);
    }
}
