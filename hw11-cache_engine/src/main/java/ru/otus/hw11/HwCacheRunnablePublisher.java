package ru.otus.hw11;

public interface HwCacheRunnablePublisher<K, V>
        extends HwCache<K, V>, HwCachePublisher<K, V>, Runnable {}
