package ru.otus.hw07.listener.homework;

import java.util.Optional;
import ru.otus.hw07.model.Message;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
