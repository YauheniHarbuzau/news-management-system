package ru.clevertec.cache;

import ru.clevertec.cache.impl.CacheFactoryImpl;

/**
 * Фабрика для инициализации алгоритма кэширования данных
 *
 * @see CacheFactoryImpl
 */
public interface CacheFactory<K, V> {

    Cache<K, V> initCache(String cacheType, int cacheCapacity);
}
