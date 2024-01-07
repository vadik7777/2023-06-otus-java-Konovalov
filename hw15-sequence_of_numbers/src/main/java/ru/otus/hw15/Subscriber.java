package ru.otus.hw15;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Subscriber extends Thread {

    private final VariableStore variableStore;

    @SuppressWarnings("java:S3776")
    @Override
    public void run() {
        var monitor = variableStore.getMonitor();
        var currentThreadName = Thread.currentThread().getName();
        synchronized (monitor) {
            while (!Thread.currentThread().isInterrupted()) {
                if (Objects.equals(currentThreadName, variableStore.getCurrentSubscriberName())) {
                    log.info("{}", variableStore.getVariable());
                    variableStore.setPrint(true);
                    monitor.notifyAll();
                    try {
                        while (Objects.equals(
                                currentThreadName, variableStore.getCurrentSubscriberName())) {
                            monitor.wait();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    monitor.notifyAll();
                    try {
                        while (!Objects.equals(
                                currentThreadName, variableStore.getCurrentSubscriberName())) {
                            monitor.wait();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
