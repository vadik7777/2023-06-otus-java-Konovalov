package ru.otus.hw16.services;

import ru.otus.hw16.api.SensorsDataChannel;
import ru.otus.hw16.api.SensorsDataServer;
import ru.otus.hw16.api.model.SensorData;

public class SensorsDataServerImpl implements SensorsDataServer {

    private final SensorsDataChannel sensorsDataChannel;

    public SensorsDataServerImpl(SensorsDataChannel sensorsDataChannel) {
        this.sensorsDataChannel = sensorsDataChannel;
    }

    @Override
    public void onReceive(SensorData sensorData) {
        sensorsDataChannel.push(sensorData);
    }
}
