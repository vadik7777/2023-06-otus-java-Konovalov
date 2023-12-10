package ru.otus.hw11;

public interface HwCachePublisher<K, V> {
    void event(K key, V value, String action);
}
