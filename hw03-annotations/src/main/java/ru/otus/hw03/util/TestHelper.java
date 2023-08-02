package ru.otus.hw03.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import ru.otus.hw03.annotation.After;
import ru.otus.hw03.annotation.Before;
import ru.otus.hw03.annotation.Test;

@SuppressWarnings({"java:S106", "java:S112" /*, "java:S3776"*/})
public class TestHelper {

    private TestHelper() {}

    public static void testClassByName(String className) throws ClassNotFoundException {
        int successCount = 0;
        int errorCount = 0;
        var clazz = Class.forName(className);
        var declaredMethods = clazz.getDeclaredMethods();
        var testMethods = getTestMethods(declaredMethods);

        for (Method method : testMethods) {
            var instance = instantiate(clazz);
            boolean success = callMethod(instance, method);
            if (success) {
                successCount++;
            } else {
                errorCount++;
            }
        }
        int all = successCount + errorCount;
        printResult(className, all, successCount, errorCount);
    }

    private static ArrayList<Method> getTestMethods(Method[] declaredMethods) {
        var testMethods = new ArrayList<Method>();
        for (Method method : declaredMethods) {
            var annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Before) {
                    testMethods.add(0, method);
                } else if (annotation instanceof Test) {
                    if (!testMethods.isEmpty()) {
                        testMethods.add(1, method);
                    } else {
                        testMethods.add(method);
                    }
                } else if (annotation instanceof After) {
                    testMethods.add(method);
                    break;
                }
            }
        }
        return testMethods;
    }

    private static boolean callMethod(Object instance, Method method, Object... args) {
        try {
            method.invoke(instance, args);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void printResult(String className, int all, int success, int error) {
        System.out.printf(
                "Test class: %s, all: %d, success: %d, error: %d;%n",
                className, all, success, error);
    }

    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
