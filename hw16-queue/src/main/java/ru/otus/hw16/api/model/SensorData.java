package ru.otus.hw16.api.model;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SensorData implements Cloneable {
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    @SuppressWarnings({"java:S2975", "java:S1182"})
    @Override
    public SensorData clone() {
        return new SensorData(this.measurementTime, this.room, this.value);
    }

    public SensorData(LocalDateTime measurementTime, String room, Double value) {
        this.measurementTime = measurementTime;
        this.room = room;
        this.value = value;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public String getRoom() {
        return room;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SensorData{"
                + "measurementTime="
                + measurementTime
                + ", room='"
                + room
                + '\''
                + ", value="
                + value
                + '}';
    }
}
