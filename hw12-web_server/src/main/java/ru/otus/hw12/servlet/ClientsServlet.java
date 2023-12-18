package ru.otus.hw12.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.hw10.crm.service.DBServiceClient;
import ru.otus.hw12.services.TemplateProcessor;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var clients = dbServiceClient.findAll();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);

        response.setContentType("text/html");
        try (var writer = response.getWriter()) {
            writer.println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
        }
    }
}
