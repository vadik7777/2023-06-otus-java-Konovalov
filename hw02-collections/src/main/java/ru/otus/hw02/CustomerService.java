package ru.otus.hw02;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> treeMap =
            new TreeMap<>(Comparator.comparing(o -> Long.valueOf(o.getScores())));

    public Map.Entry<Customer, String> getSmallest() {
        var entry = treeMap.firstEntry();
        return generateEntryCopy(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var entry = treeMap.higherEntry(customer);
        return generateEntryCopy(entry);
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }

    private Map.Entry<Customer, String> generateEntryCopy(Map.Entry<Customer, String> entry) {
        if (Objects.isNull(entry)) {
            return null;
        }
        var nextCustomer = entry.getKey();
        var nextData = entry.getValue();
        return Map.entry(
                new Customer(
                        nextCustomer.getId(), nextCustomer.getName(), nextCustomer.getScores()),
                nextData);
    }
}
