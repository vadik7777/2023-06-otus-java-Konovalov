package ru.otus.hw06;

import java.util.List;
import ru.otus.hw06.model.Banknote;

public interface Atm {

    int getBalance();

    void addBanknote(List<Banknote> banknoteList);

    void getMoney(int money);
}
