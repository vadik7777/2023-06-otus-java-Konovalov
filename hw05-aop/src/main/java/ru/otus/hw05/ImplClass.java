package ru.otus.hw05;

@SuppressWarnings("java:S1186")
public class ImplClass implements InterfaceClass {

    @Log
    @Override
    public void calculation(int param) {}

    @Override
    public void calculation(int param1, int param2, String param3) {}

    @Log
    @Override
    public void calculation(int param1, int param2, String param3, String param4) {}
}
