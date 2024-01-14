package ru.otus.hw17.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.NewValueMessage;
import ru.otus.hw17.RemoteValueGeneratorGrpc;
import ru.otus.hw17.StartGenerate;

public class GRPCServerService extends RemoteValueGeneratorGrpc.RemoteValueGeneratorImplBase {

    private static final Logger logger = LoggerFactory.getLogger(GRPCServerService.class);

    private final ExecutorService threadPool = Executors.newFixedThreadPool(3);

    @SuppressWarnings({"java:S125", "java:S1602"})
    @Override
    public void generateValue(
            StartGenerate request, StreamObserver<NewValueMessage> responseObserver) {

        var scheduledThreadPool = Executors.newScheduledThreadPool(1);
        var task =
                new Runnable() {
                    int value = request.getFirstValue();
                    int lastValue = request.getLastValue();

                    @Override
                    public void run() {
                        if (value < lastValue) {
                            value++;
                            logger.info("Generate new value: {}", value);
                            responseObserver.onNext(
                                    NewValueMessage.newBuilder().setNewValue(value).build());
                        } else {
                            scheduledThreadPool.shutdown();
                            responseObserver.onCompleted();
                        }
                    }
                };
        threadPool.submit(
                () -> {
                    scheduledThreadPool.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
                });
    }
}
