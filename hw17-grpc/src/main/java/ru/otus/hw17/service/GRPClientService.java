package ru.otus.hw17.service;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.NewValueMessage;
import ru.otus.hw17.RemoteValueGeneratorGrpc;
import ru.otus.hw17.StartGenerate;

public class GRPClientService {

    private static final Logger logger = LoggerFactory.getLogger(GRPClientService.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIST_VALUE_GENERATE = 0;
    private static final int LAST_VALUE_GENERATE = 30;
    private static final int FIST_VALUE_CYCLE = 0;
    private static final int LAST_VALUE_CYCLE = 50;

    private volatile int newValue;
    private int currentValue;
    private int oldValue;

    public void start() {
        var latch = new CountDownLatch(2);
        var channel =
                ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        var stub = RemoteValueGeneratorGrpc.newStub(channel);
        stub.generateValue(
                StartGenerate.newBuilder()
                        .setFirstValue(FIST_VALUE_GENERATE)
                        .setLastValue(LAST_VALUE_GENERATE)
                        .build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(NewValueMessage newValueMessage) {
                        synchronized (this) {
                            newValue = newValueMessage.getNewValue();
                        }
                        logger.info("New value: {}", newValue);
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.error("Got error: {}", t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("Server stop generate!!!");
                        latch.countDown();
                    }
                });
        var scheduledThreadPool = Executors.newScheduledThreadPool(1);
        var task =
                new Runnable() {
                    int repeatCount = LAST_VALUE_CYCLE - FIST_VALUE_CYCLE;

                    public void run() {
                        synchronized (this) {
                            if (newValue != oldValue) {
                                oldValue = newValue;
                                currentValue = currentValue + newValue + 1;
                            } else {
                                currentValue++;
                            }
                        }
                        logger.info("Current value: {}", currentValue);
                        if (needStop()) {
                            logger.info("Client stop calculate!!!");
                            scheduledThreadPool.shutdown();
                            latch.countDown();
                        }
                    }

                    private boolean needStop() {
                        if (repeatCount > 0) {
                            repeatCount--;
                            return false;
                        }
                        return true;
                    }
                };
        scheduledThreadPool.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            channel.shutdown();
        }
    }
}
