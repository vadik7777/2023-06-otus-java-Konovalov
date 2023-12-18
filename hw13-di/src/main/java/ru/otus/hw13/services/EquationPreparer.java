package ru.otus.hw13.services;

import java.util.List;
import ru.otus.hw13.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
