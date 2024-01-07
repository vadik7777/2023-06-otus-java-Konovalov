package ru.otus.hw15;

import java.util.List;

public class SequenceDemo {

    private static final String SUBSCRIBER_1 = "SUBSCRIBER_1";
    private static final String SUBSCRIBER_2 = "SUBSCRIBER_2";

    public static void main(String[] args) {
        new SequenceDemo().demo();
    }

    private void demo() {
        var variableStore = new VariableStore();
        var subscribers = initSubscribers(variableStore);
        var publisher = new Publisher(variableStore, subscribers);

        for (int i = 1; i < 10; i++) {
            publisher.print(i);
        }
        for (int i = 10; i > 0; i--) {
            publisher.print(i);
        }
        for (var subscriber : subscribers) {
            subscriber.interrupt();
        }
    }

    private List<Thread> initSubscribers(VariableStore variableStore) {
        var subscriberFactory = new SubscriberFactory();
        var subscriber1 = subscriberFactory.createSubscriber(SUBSCRIBER_1, variableStore);
        var subscriber2 = subscriberFactory.createSubscriber(SUBSCRIBER_2, variableStore);
        return List.of(subscriber1, subscriber2);
    }
}
