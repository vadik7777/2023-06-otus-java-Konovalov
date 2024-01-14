package ru.otus.hw17;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.service.GRPCServerService;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var grpcServerService = new GRPCServerService();

        var server = ServerBuilder.forPort(SERVER_PORT).addService(grpcServerService).build();
        logger.info("server waiting for client connections...");
        server.start();
        server.awaitTermination();
    }
}
