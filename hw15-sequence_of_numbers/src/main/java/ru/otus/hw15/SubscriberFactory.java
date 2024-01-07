package ru.otus.hw15;

public class SubscriberFactory {

    public Thread createSubscriber(String name, VariableStore variableStore) {
        var subscriber = new Subscriber(variableStore);
        subscriber.setName(name);
        subscriber.start();
        var monitor = variableStore.getMonitor();
        synchronized (monitor) {
            while (!subscriber.isAlive()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return subscriber;
    }
}
