package ru.hw08.dataprocessor;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ru.hw08.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        return new TreeMap<>(
                data.stream()
                        .collect(groupingBy(Measurement::name, summingDouble(Measurement::value))));
    }
}
