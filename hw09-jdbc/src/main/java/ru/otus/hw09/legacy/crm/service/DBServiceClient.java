package ru.otus.hw09.legacy.crm.service;

import java.util.List;
import java.util.Optional;
import ru.otus.hw09.legacy.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
