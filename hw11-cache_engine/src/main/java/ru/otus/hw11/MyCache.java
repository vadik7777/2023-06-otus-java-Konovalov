package ru.otus.hw11;

import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCacheRunnablePublisher<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final WeakHashMap<K, V> weakHashMap = new WeakHashMap<>();
    private final List<Reference<HwListener<K, V>>> softListeners = new CopyOnWriteArrayList<>();
    private final List<Reference<HwListener<K, V>>> phantomListeners = new CopyOnWriteArrayList<>();
    private final ReferenceQueue<HwListener<K, V>> phantomReferencesQueue = new ReferenceQueue<>();

    @Override
    public void put(K key, V value) {
        weakHashMap.put(key, value);
        event(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        var value = weakHashMap.remove(key);
        event(key, value, "DELETE");
    }

    @Override
    public V get(K key) {
        var value = weakHashMap.get(key);
        event(key, value, "GET");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        var softReference = new SoftReference<>(listener);
        softListeners.add(softReference);
        var phantomReference = new PhantomReference<>(listener, phantomReferencesQueue);
        phantomListeners.add(phantomReference);
        logger.info("Add listener: {}, value: {}", softReference, softReference.get());
        event(null, null, "ADD_LISTENER");
    }

    @SuppressWarnings({"java:S1854", "java:S1226"})
    @Override
    public void removeListener(HwListener<K, V> listener) {
        listener = null;
        event(null, null, "REMOVE_LISTENER");
    }

    @Override
    public void event(K key, V value, String action) {
        for (Reference<HwListener<K, V>> softReference : softListeners) {
            var listener = softReference.get();
            if (Objects.nonNull(listener)) {
                listener.notify(key, value, action);
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Reference<? extends HwListener<K, V>> reference;
            while ((reference = phantomReferencesQueue.poll()) != null) {
                phantomListeners.remove(reference);
                var softListenersToRemove =
                        softListeners.stream()
                                .filter(soft -> Objects.isNull(soft.get()))
                                .findFirst()
                                .orElseThrow();
                softListeners.remove(softListenersToRemove);
                logger.info(
                        "Delete listener: {}, value: {}",
                        softListenersToRemove,
                        softListenersToRemove.get());
            }
        }
    }
}
