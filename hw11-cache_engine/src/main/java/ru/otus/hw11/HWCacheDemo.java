package ru.otus.hw11;

import java.util.ArrayList;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.core.repository.DataTemplateHibernate;
import ru.otus.hw10.core.repository.HibernateUtils;
import ru.otus.hw10.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.hw10.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.hw10.crm.model.Address;
import ru.otus.hw10.crm.model.Client;
import ru.otus.hw10.crm.model.Phone;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
        new HWCacheDemo().bigTest();
        new HWCacheDemo().dbTest();
    }

    private void demo() {
        HwCacheRunnablePublisher<String, Integer> cache = new MyCache<>();
        var hwCacheThread = new Thread(cache);
        hwCacheThread.start();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener =
                new HwListener<String, Integer>() {
                    @Override
                    public void notify(String key, Integer value, String action) {
                        logger.info("key:{}, value:{}, action: {}", key, value, action);
                    }
                };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
        hwCacheThread.interrupt();
    }

    private void bigTest() {
        HwCacheRunnablePublisher<String, BigObject> cache = new MyCache<>();
        var hwCacheThread = new Thread(cache);
        hwCacheThread.start();

        for (int i = 0; i < 100; i++) {
            @SuppressWarnings({"java:S1604", "java:S1186"})
            HwListener<String, BigObject> listener =
                    new HwListener<String, BigObject>() {
                        BigObject bigObject = new BigObject();

                        @Override
                        public void notify(String key, BigObject value, String action) {}
                    };

            cache.addListener(listener);
            cache.removeListener(listener);
            cache.put("key" + i, new BigObject());
            cache.get("key" + i);
        }
        hwCacheThread.interrupt();
    }

    @SuppressWarnings("java:S1215")
    private void dbTest() {
        HwCacheRunnablePublisher<String, Client> cache = new MyCache<>();
        var hwCacheThread = new Thread(cache);
        hwCacheThread.start();

        @SuppressWarnings("java:S1604")
        HwListener<String, Client> listener =
                new HwListener<String, Client>() {
                    @Override
                    public void notify(String key, Client value, String action) {
                        logger.info("key:{}, value:{}, action: {}", key, value, action);
                    }
                };
        cache.addListener(listener);

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
        var dbServiceClient =
                new DbServiceClientWithCacheImpl(transactionManager, clientTemplate, cache);

        var keyList = new ArrayList<Long>(10);
        for (int i = 0; i < 10; i++) {
            var client = dbServiceClient.saveClient(new Client("Client" + i));
            keyList.add(client.getId());
        }
        keyList.forEach(dbServiceClient::getClient);
        System.gc();
        keyList.forEach(dbServiceClient::getClient);

        cache.removeListener(listener);
        hwCacheThread.interrupt();
    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];
    }
}
