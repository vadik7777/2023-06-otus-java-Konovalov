package ru.otus.hw15;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableStore {

    private final Object monitor = new Object();

    private volatile int variable;
    private volatile String currentSubscriberName;
    private volatile boolean isPrint = false;
}
