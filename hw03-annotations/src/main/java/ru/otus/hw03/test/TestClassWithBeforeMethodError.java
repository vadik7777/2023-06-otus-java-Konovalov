package ru.otus.hw03.test;

import ru.otus.hw03.annotation.After;
import ru.otus.hw03.annotation.Before;
import ru.otus.hw03.annotation.Test;

@SuppressWarnings({"java:S1186", "java:S112"})
public class TestClassWithBeforeMethodError {

    @After
    public void afterMethod() {}

    @Test
    public void testMethod() {}

    @Before
    public void beforeMethod() {
        throw new RuntimeException("exception in beforeMethod");
    }
}
