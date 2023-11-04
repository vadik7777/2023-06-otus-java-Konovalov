package ru.otus.hw06.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.otus.hw06.model.Banknote.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.Atm;
import ru.otus.hw06.Cassette;

@DisplayName("Банкомат должен")
class AtmImplTest {

    private Cassette cassetteTen = mock(Cassette.class);
    ;
    private Cassette cassetteFifty = mock(Cassette.class);
    ;
    private Cassette cassetteOneHundred = mock(Cassette.class);
    ;
    private Cassette cassetteTwoHundred = mock(Cassette.class);
    ;
    private Cassette cassetteFiveHundred = mock(Cassette.class);

    private Atm atm;

    @BeforeEach
    void setUp() {
        when(cassetteFiveHundred.getBanknote()).thenReturn(FIVE_HUNDRED);
        when(cassetteTwoHundred.getBanknote()).thenReturn(TWO_HUNDRED);
        when(cassetteOneHundred.getBanknote()).thenReturn(ONE_HUNDRED);
        when(cassetteFifty.getBanknote()).thenReturn(FIFTY);
        when(cassetteTen.getBanknote()).thenReturn(TEN);

        // 1080
        when(cassetteFiveHundred.getCount()).thenReturn(1);
        when(cassetteTwoHundred.getCount()).thenReturn(1);
        when(cassetteOneHundred.getCount()).thenReturn(2);
        when(cassetteFifty.getCount()).thenReturn(3);
        when(cassetteTen.getCount()).thenReturn(3);

        atm =
                new AtmImpl(
                        List.of(
                                cassetteTen,
                                cassetteFifty,
                                cassetteOneHundred,
                                cassetteTwoHundred,
                                cassetteFiveHundred));
    }

    @DisplayName("правильно отдавать баланс")
    @Test
    void shouldCorrectGetBalance() {
        assertEquals(1080, atm.getBalance());
    }

    @DisplayName("правильно добавлять банкноты")
    @Test
    void shouldCorrectAddBanknote() {
        atm.addBanknote(List.of(TEN, TEN, FIVE_HUNDRED));
        assertAll(
                () -> verify(cassetteTen, times(2)).addBanknote(),
                () -> verify(cassetteFiveHundred, times(1)).addBanknote());
    }

    @DisplayName("правильно выдавать деньги")
    @Test
    void shouldCorrectGetMoney() {
        atm.getMoney(120);
        assertAll(
                () -> verify(cassetteOneHundred, times(1)).removeBanknote(),
                () -> verify(cassetteTen, times(2)).removeBanknote());
    }

    @DisplayName("не выдавать деньги")
    @Test
    void shouldNotCorrectGetMoney() {
        assertThrows(RuntimeException.class, () -> atm.getMoney(140));
    }
}
