package ru.clevertec.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.cache.Cache;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для {@link LFUCache}
 */
class LFUCacheTest {

    @Test
    void checkPutShouldWorkCorrectly() {
        // given
        int capacity = 2;
        Cache<Long, Integer> cache = new LFUCache<>(capacity);

        // when
        cache.put(1L, 10);
        cache.get(1L);
        cache.put(2L, 20);
        cache.put(3L, 30);

        // then
        assertAll(
                () -> assertTrue(cache.get(1L).isPresent()),
                () -> assertFalse(cache.get(2L).isPresent()),
                () -> assertTrue(cache.get(3L).isPresent()),
                () -> assertEquals(2, cache.size())
        );
    }

    @Test
    void checkGetShouldWorkCorrectly() {
        // given
        int capacity = 2;
        Cache<Long, Integer> cache = new LFUCache<>(capacity);

        // when
        cache.put(1L, 10);
        cache.get(1L);
        cache.put(2L, 20);
        cache.put(3L, 30);

        // then
        assertAll(
                () -> assertEquals(Optional.of(10), cache.get(1L)),
                () -> assertNotEquals(Optional.of(20), cache.get(2L)),
                () -> assertEquals(Optional.of(30), cache.get(3L)),
                () -> assertEquals(2, cache.size())
        );
    }

    @Test
    void checkRemoveShouldWorkCorrectly() {
        // given
        int capacity = 3;
        Cache<Long, Integer> cache = new LFUCache<>(capacity);

        // when
        cache.put(1L, 10);
        cache.put(2L, 20);
        cache.put(3L, 30);
        cache.remove(3L);

        // then
        assertAll(
                () -> assertTrue(cache.get(1L).isPresent()),
                () -> assertTrue(cache.get(2L).isPresent()),
                () -> assertFalse(cache.get(3L).isPresent()),
                () -> assertEquals(2, cache.size())
        );
    }
}
