package ru.otus.hw15;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Publisher {

    private final VariableStore variableStore;
    private final List<Thread> subscribers;

    public void print(int variable) {
        var monitor = variableStore.getMonitor();
        synchronized (monitor) {
            variableStore.setVariable(variable);
            for (var subscriber : subscribers) {
                var subscriberName = subscriber.getName();
                variableStore.setPrint(false);
                variableStore.setCurrentSubscriberName(subscriberName);
                monitor.notifyAll();
                try {
                    while (!variableStore.isPrint()) {
                        monitor.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
