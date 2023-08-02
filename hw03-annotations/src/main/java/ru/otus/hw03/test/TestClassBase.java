package ru.otus.hw03.test;

import ru.otus.hw03.annotation.*;

@SuppressWarnings("java:S1186")
public class TestClassBase {

    @Before
    public void beforeMethod() {}

    @Test
    public void testMethod() {}

    @After
    public void afterMethod() {}
}
