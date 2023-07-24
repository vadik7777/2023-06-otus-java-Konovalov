package ru.otus;

import com.google.common.collect.Lists;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String... args) {
        var numbers = Lists.newArrayList(1, 2, 3, 4, 5);
        Lists.reverse(numbers).forEach(System.out::println);
    }
}
