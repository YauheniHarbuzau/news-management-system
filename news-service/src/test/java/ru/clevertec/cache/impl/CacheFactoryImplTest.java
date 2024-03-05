package ru.clevertec.cache.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.exception.CacheAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для {@link CacheFactoryImpl}
 */
class CacheFactoryImplTest {

    private final CacheFactory<Object, Object> cacheFactory = new CacheFactoryImpl<>();

    @Test
    void checkInitCacheShouldInitializeLRUCache() {
        // given, when
        Cache<Object, Object> cache = cacheFactory.initCache("LRU", 10);

        // then
        assertAll(
                () -> assertTrue(cache instanceof LRUCache<Object, Object>),
                () -> assertFalse(cache instanceof LFUCache<Object, Object>)
        );
    }

    @Test
    void checkInitCacheShouldInitializeLFUCache() {
        // given, when
        Cache<Object, Object> cache = cacheFactory.initCache("LFU", 10);

        // then
        assertAll(
                () -> assertTrue(cache instanceof LFUCache<Object, Object>),
                () -> assertFalse(cache instanceof LRUCache<Object, Object>)
        );
    }

    @Test
    void checkInitCacheShouldThrowCacheAlgorithmException() {
        // given, when
        Executable cache = () -> cacheFactory.initCache("Unknown Cache Algorithm", 10);

        // then
        assertThrows(CacheAlgorithmException.class, cache);
    }
}
