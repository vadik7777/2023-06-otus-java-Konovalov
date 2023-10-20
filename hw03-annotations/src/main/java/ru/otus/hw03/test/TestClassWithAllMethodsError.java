package ru.otus.hw03.test;

import ru.otus.hw03.annotation.After;
import ru.otus.hw03.annotation.Before;
import ru.otus.hw03.annotation.Test;

@SuppressWarnings({"java:S1186", "java:S112"})
public class TestClassWithAllMethodsError {

    @Before
    public void beforeMethod() {
        throw new RuntimeException("exception in beforeMethod");
    }

    @Test
    public void testMethod() {
        throw new RuntimeException("exception in testMethod");
    }

    @After
    public void afterMethod() {
        throw new RuntimeException("exception in afterMethod");
    }
}
