package ru.otus.hw02;

import java.util.Iterator;
import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> linkedList = new LinkedList<>();
    private Iterator<Customer> iterator;

    public void add(Customer customer) {
        linkedList.addFirst(customer);
        iterator = linkedList.iterator();
    }

    public Customer take() {
        return iterator.next();
    }
}
