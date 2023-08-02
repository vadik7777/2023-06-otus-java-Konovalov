package ru.otus.hw03;

import java.util.List;
import ru.otus.hw03.util.TestHelper;

public class AnnotationDemo {

    public static void main(String... args) throws ClassNotFoundException {
        var testClasses =
                List.of(
                        "ru.otus.hw03.test.TestClassBase",
                        "ru.otus.hw03.test.TestClassWithAfterMethodError",
                        "ru.otus.hw03.test.TestClassWithAllMethodsError",
                        "ru.otus.hw03.test.TestClassWithBeforeMethodError",
                        "ru.otus.hw03.test.TestClassWithTestMethodError");

        for (String testClass : testClasses) {
            TestHelper.testClassByName(testClass);
        }
    }
}
