package ru.otus.hw07.listener.homework;

import java.util.*;
import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        var field13Copy = new ObjectForMessage(msg.getField13());
        history.put(msg.getId(), msg.toBuilder().field13(field13Copy).build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}
