package ru.otus.hw06.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.Cassette;
import ru.otus.hw06.model.Banknote;

@DisplayName("Кассета банкомата должна")
class CassetteImplTest {

    private Cassette cassette;

    @BeforeEach
    public void setUp() {
        cassette = new CassetteImpl(Banknote.FIFTY);
        cassette.addBanknote();
    }

    @DisplayName("правильно возвращать тип банкноты")
    @Test
    void shouldCorrectGetBanknote() {
        assertEquals(Banknote.FIFTY, cassette.getBanknote());
    }

    @DisplayName("правильно возвращать количество банкнот")
    @Test
    void shouldCorrectGetCount() {
        assertEquals(1, cassette.getCount());
    }

    @DisplayName("правильно добавлять банкноту")
    @Test
    void shouldCorrectAddBanknote() {
        cassette.addBanknote();
        assertEquals(2, cassette.getCount());
    }

    @DisplayName("правильно удалять банкноту")
    @Test
    void shouldCorrectRemoveBanknote() {
        cassette.removeBanknote();
        assertEquals(0, cassette.getCount());
    }
}
