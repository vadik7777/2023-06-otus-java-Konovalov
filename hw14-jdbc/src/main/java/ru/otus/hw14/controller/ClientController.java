package ru.otus.hw14.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw14.service.ClientService;

@RequiredArgsConstructor
@Controller
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/")
    public String getClients(Model model) {
        var clients = clientService.getAll();
        model.addAttribute("clients", clients);
        return "clients";
    }
}
