package ru.otus.hw06.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import ru.otus.hw06.Atm;
import ru.otus.hw06.Cassette;
import ru.otus.hw06.model.Banknote;

public class AtmImpl implements Atm {

    private final TreeMap<Banknote, Cassette> cassettes;

    public AtmImpl(List<Cassette> cassettes) {
        this.cassettes = new TreeMap<>(Comparator.comparingInt(Banknote::getNominal).reversed());
        for (var cassette : cassettes) {
            this.cassettes.put(cassette.getBanknote(), cassette);
        }
    }

    @Override
    public int getBalance() {
        return cassettes.entrySet().stream()
                .map(entry -> entry.getKey().getNominal() * entry.getValue().getCount())
                .reduce(Integer::sum)
                .orElse(0);
    }

    @Override
    public void addBanknote(List<Banknote> banknotes) {
        banknotes.forEach(banknote -> cassettes.get(banknote).addBanknote());
    }

    @SuppressWarnings("java:S112")
    @Override
    public void getMoney(int money) {
        var removeCassetteCountMap = new HashMap<Cassette, Integer>();
        for (var cassette : cassettes.values()) {
            int removeFromCassetteCount = getCountByCassetteForRemove(money, cassette);
            if (removeFromCassetteCount > 0) {
                money -= cassette.getBanknote().getNominal() * removeFromCassetteCount;
                removeCassetteCountMap.put(cassette, removeFromCassetteCount);
            }
            if (money == 0) {
                break;
            }
        }
        if (money == 0) {
            for (var removeCassetteCount : removeCassetteCountMap.entrySet()) {
                var cassette = removeCassetteCount.getKey();
                var removeCount = removeCassetteCount.getValue();
                for (int i = 0; i < removeCount; i++) {
                    cassette.removeBanknote();
                }
            }
        } else {
            throw new RuntimeException("Money can't be taken");
        }
    }

    private int getCountByCassetteForRemove(int money, Cassette cassette) {
        var nominal = cassette.getBanknote().getNominal();
        var count = cassette.getCount();
        var needCount = money / nominal;
        return Math.min(count, needCount);
    }
}
