package ru.otus.hw14.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw14.model.Client;
import ru.otus.hw14.repository.ClientRepository;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Client> getAll() {
        return clientRepository.findAllFull().stream().map(Client::clone).toList();
    }

    @Transactional
    @Override
    public Client save(Client client) {
        return clientRepository.save(client).clone();
    }
}
