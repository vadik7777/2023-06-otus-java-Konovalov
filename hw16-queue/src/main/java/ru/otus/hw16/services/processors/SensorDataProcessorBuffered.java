package ru.otus.hw16.services.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.api.SensorDataProcessor;
import ru.otus.hw16.api.model.SensorData;
import ru.otus.hw16.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final ConcurrentSkipListSet<SensorData> concurrentSkipListSet;
    private final List<SensorData> bufferedData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.bufferedData = new ArrayList<>();
        this.concurrentSkipListSet =
                new ConcurrentSkipListSet<>(
                        (d1, d2) ->
                                d1.getMeasurementTime().isAfter(d2.getMeasurementTime()) ? 1 : -1);
    }

    @Override
    public void process(SensorData data) {
        if (concurrentSkipListSet.size() >= bufferSize) {
            flush();
        }
        concurrentSkipListSet.add(data);
    }

    public synchronized void flush() {
        try {
            while (!concurrentSkipListSet.isEmpty()) {
                var sensorData = concurrentSkipListSet.pollFirst();
                if (sensorData != null) {
                    bufferedData.add(sensorData);
                }
            }
            if (!bufferedData.isEmpty()) {
                writer.writeBufferedData(bufferedData.stream().map(SensorData::clone).toList());
                bufferedData.clear();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
