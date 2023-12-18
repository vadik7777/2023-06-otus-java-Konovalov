package ru.otus.hw12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.hw10.core.repository.DataTemplateHibernate;
import ru.otus.hw10.core.repository.HibernateUtils;
import ru.otus.hw10.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hw10.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.hw10.crm.model.Address;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.model.Phone;
import ru.otus.hw10.crm.service.DbServiceClientImpl;
import ru.otus.hw12.helpers.FileSystemHelper;
import ru.otus.hw12.server.ClientsWebServer;
import ru.otus.hw12.services.TemplateProcessor;
import ru.otus.hw12.services.TemplateProcessorImpl;

public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure("hibernate.cfg.xml");
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory =
                HibernateUtils.buildSessionFactory(
                        configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(
                        HASH_LOGIN_SERVICE_CONFIG_NAME);
        HashLoginService loginService =
                new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        loginService.start();

        ClientsWebServer clientsWebServer =
                new ClientsWebServer(
                        WEB_SERVER_PORT, loginService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
