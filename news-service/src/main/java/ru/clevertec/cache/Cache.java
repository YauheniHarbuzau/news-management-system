package ru.clevertec.cache;

import ru.clevertec.cache.impl.LFUCache;
import ru.clevertec.cache.impl.LRUCache;

import java.util.Optional;

/**
 * Интерфейс для алгоритмов кэширования данных
 *
 * @see LRUCache
 * @see LFUCache
 */
public interface Cache<K, V> {

    void put(K key, V value);

    Optional<V> get(K key);

    void remove(K key);

    int size();
}
