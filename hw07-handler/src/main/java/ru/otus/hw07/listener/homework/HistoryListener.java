package ru.otus.hw07.listener.homework;

import java.util.Optional;
import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    @Override
    public void onUpdated(Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        throw new UnsupportedOperationException();
    }
}
