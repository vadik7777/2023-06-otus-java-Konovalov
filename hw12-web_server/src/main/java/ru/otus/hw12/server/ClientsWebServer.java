package ru.otus.hw12.server;

import com.google.gson.Gson;
import java.util.Arrays;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.hw10.crm.service.DBServiceClient;
import ru.otus.hw12.helpers.FileSystemHelper;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.servlet.AuthorizationFilter;
import ru.otus.hw12.servlet.ClientApiServlet;
import ru.otus.hw12.servlet.ClientsServlet;
import ru.otus.hw12.servlet.LoginServlet;

public class ClientsWebServer implements WebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final LoginService loginService;
    private final DBServiceClient dbServiceClient;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public ClientsWebServer(
            int port,
            LoginService loginService,
            DBServiceClient dbServiceClient,
            Gson gson,
            TemplateProcessor templateProcessor) {
        this.loginService = loginService;
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients", "/api/client/*"));

        server.setHandler(handlers);
        return server;
    }

    @SuppressWarnings({"squid:S1172"})
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(
                new ServletHolder(new LoginServlet(templateProcessor, loginService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path ->
                                servletContextHandler.addFilter(
                                        new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] {START_PAGE_NAME});
        resourceHandler.setResourceBase(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    @SuppressWarnings("java:S125")
    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(
                new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)),
                "/clients");
        servletContextHandler.addServlet(
                new ServletHolder(new ClientApiServlet(gson, dbServiceClient)), "/api/client/*");
        return servletContextHandler;
    }
}
