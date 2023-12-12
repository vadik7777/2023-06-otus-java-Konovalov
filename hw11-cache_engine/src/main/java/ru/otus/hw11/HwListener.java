package ru.otus.hw11;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
