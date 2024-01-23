package ru.otus.reactive.datastore.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.reactive.datastore.domain.Message;

public interface DataStore {

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);
}
