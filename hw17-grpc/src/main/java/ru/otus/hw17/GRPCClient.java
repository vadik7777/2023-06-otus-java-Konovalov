package ru.otus.hw17;

import ru.otus.hw17.service.GRPClientService;

public class GRPCClient {

    public static void main(String[] args) {
        var grpcClientService = new GRPClientService();
        grpcClientService.start();
    }
}
