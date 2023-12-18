package ru.otus.hw12.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.service.DBServiceClient;

@SuppressWarnings({"squid:S1948"})
public class ClientApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientApiServlet(Gson gson, DBServiceClient dbServiceClient) {
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try (var inputStream = request.getInputStream()) {
            var clientJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            var client = gson.fromJson(clientJson, Client.class);
            dbServiceClient.saveClient(client);
        }
        response.sendRedirect("/clients");
    }
}
