package ru.otus.hw14.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw14.model.Client;
import ru.otus.hw14.service.ClientService;

@RequiredArgsConstructor
@RestController
public class ClientRestController {

    private final ClientService clientService;

    @GetMapping("/api/client")
    public List<Client> getClients() {
        return clientService.getAll();
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return clientService.save(client);
    }
}
