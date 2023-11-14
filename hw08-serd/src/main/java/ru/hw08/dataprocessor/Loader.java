package ru.hw08.dataprocessor;

import java.util.List;
import ru.hw08.model.Measurement;

public interface Loader {

    List<Measurement> load();
}
