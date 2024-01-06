package ru.otus.hw16.api;

import ru.otus.hw16.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
