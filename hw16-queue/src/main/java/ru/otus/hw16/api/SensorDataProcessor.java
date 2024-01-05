package ru.otus.hw16.api;

import ru.otus.hw16.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {}
}
