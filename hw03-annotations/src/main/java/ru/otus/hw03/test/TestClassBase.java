package ru.otus.hw03.test;

import ru.otus.hw03.annotation.*;

@SuppressWarnings({"java:S1186", "java:S112"})
public class TestClassBase {

    @Before
    public void beforeMethod() {}

    @Test
    public void testMethod() {}

    @Test
    public void testMethod1() {
        throw new RuntimeException();
    }

    @Test
    public void testMethod2() {}

    @After
    public void afterMethod() {}
}
