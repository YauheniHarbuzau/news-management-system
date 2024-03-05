package ru.clevertec.cache.impl;

import ru.clevertec.constant.Constant;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.exception.CacheAlgorithmException;

import static ru.clevertec.constant.Constant.LFU;
import static ru.clevertec.constant.Constant.LRU;

/**
 * Имплементация фабрики для инициализации алгоритма кэширования данных
 *
 * @see CacheFactory
 * @see LRUCache
 * @see LFUCache
 * @see Constant
 */
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Override
    public Cache<K, V> initCache(String cacheType, int cacheCapacity) {
        return switch (cacheType) {
            case LRU -> new LRUCache<>(cacheCapacity);
            case LFU -> new LFUCache<>(cacheCapacity);
            default -> throw CacheAlgorithmException.of(cacheType);
        };
    }
}
