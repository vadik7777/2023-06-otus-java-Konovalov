package ru.otus.hw11;

import java.util.Objects;
import java.util.Optional;
import ru.otus.hw10.core.repository.DataTemplate;
import ru.otus.hw10.core.sessionmanager.TransactionManager;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.service.DbServiceClientImpl;

public class DbServiceClientWithCacheImpl extends DbServiceClientImpl {

    private final HwCacheRunnablePublisher<String, Client> hwCacheRunnablePublisher;

    public DbServiceClientWithCacheImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> clientDataTemplate,
            HwCacheRunnablePublisher<String, Client> hwCacheRunnablePublisher) {
        super(transactionManager, clientDataTemplate);
        this.hwCacheRunnablePublisher = hwCacheRunnablePublisher;
    }

    @Override
    public Client saveClient(Client client) {
        client = super.saveClient(client);
        hwCacheRunnablePublisher.put(String.valueOf(client.getId()), client);
        return client;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var client = hwCacheRunnablePublisher.get(String.valueOf(id));
        if (Objects.isNull(client)) {
            try {
                hwCacheRunnablePublisher.event(null, null, "LONG_DATABASE_CONNECT...");
                Thread.sleep(1000);
                client = super.getClient(id).orElse(null);
                hwCacheRunnablePublisher.put(String.valueOf(id), client);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return Optional.ofNullable(client);
    }
}
