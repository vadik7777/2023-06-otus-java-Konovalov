package ru.otus.hw14.service;

import java.util.List;
import ru.otus.hw14.model.Client;

public interface ClientService {

    List<Client> getAll();

    Client save(Client client);
}
