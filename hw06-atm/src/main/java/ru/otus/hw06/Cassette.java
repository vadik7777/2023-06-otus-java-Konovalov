package ru.otus.hw06;

import ru.otus.hw06.model.Banknote;

public interface Cassette {

    Banknote getBanknote();

    int getCount();

    void addBanknote();

    void removeBanknote();
}
