package ru.otus.hw07.listener.homework;

import java.util.*;
import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        var msgCopy = msg.copy();
        history.put(msgCopy.getId(), msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}
