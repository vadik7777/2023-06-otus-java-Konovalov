package ru.otus.hw16.lib;

import java.util.List;
import ru.otus.hw16.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
