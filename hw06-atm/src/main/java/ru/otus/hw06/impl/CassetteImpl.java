package ru.otus.hw06.impl;

import ru.otus.hw06.Cassette;
import ru.otus.hw06.model.Banknote;

public class CassetteImpl implements Cassette {

    private final Banknote banknote;

    private int count;

    public CassetteImpl(Banknote banknote) {
        this.banknote = banknote;
    }

    public Banknote getBanknote() {
        return banknote;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void addBanknote() {
        count++;
    }

    @Override
    public void removeBanknote() {
        count--;
    }
}
