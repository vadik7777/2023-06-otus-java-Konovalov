package ru.otus.hw05;

public class ProxyDemo {
    public static void main(String[] args) {
        var logProxy = (InterfaceClass) LogProxyBuilder.createLogProxy(ImplClass.class);
        logProxy.calculation(5);
        logProxy.calculation(1, 2, "3");
        logProxy.calculation(1, 2, "3", "4");
    }
}
