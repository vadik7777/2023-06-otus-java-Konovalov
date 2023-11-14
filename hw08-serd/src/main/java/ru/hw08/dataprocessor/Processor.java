package ru.hw08.dataprocessor;

import java.util.List;
import java.util.Map;
import ru.hw08.model.Measurement;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
