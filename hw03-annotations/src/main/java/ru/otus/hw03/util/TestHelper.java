package ru.otus.hw03.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import ru.otus.hw03.annotation.After;
import ru.otus.hw03.annotation.Before;
import ru.otus.hw03.annotation.Test;

@SuppressWarnings({"java:S106", "java:S112"})
public class TestHelper {

    private TestHelper() {}

    public static void testClassByName(String className) throws ClassNotFoundException {
        int successCount = 0;
        int errorCount = 0;
        var clazz = Class.forName(className);
        var declaredMethods = clazz.getDeclaredMethods();
        var testMap = initTestMap(declaredMethods);

        for (Method method : testMap.get(Test.class)) {
            var instance = instantiate(clazz);
            var beforeMethodsSuccessList =
                    testMap.get(Before.class).stream()
                            .map(beforeMethod -> callMethod(instance, beforeMethod))
                            .toList();
            var beforeMethodsSuccess =
                    beforeMethodsSuccessList.stream()
                            .filter(success -> !success)
                            .findFirst()
                            .orElse(true);
            var testMethodSuccess = callMethod(instance, method);
            var afterMethodsSuccessList =
                    testMap.get(After.class).stream()
                            .map(afterMethod -> callMethod(instance, afterMethod))
                            .toList();
            var afterMethodsSuccess =
                    afterMethodsSuccessList.stream()
                            .filter(success -> !success)
                            .findFirst()
                            .orElse(true);

            if (beforeMethodsSuccess.booleanValue()
                    && testMethodSuccess
                    && afterMethodsSuccess.booleanValue()) {
                successCount++;
            } else {
                errorCount++;
            }
        }

        int all = successCount + errorCount;
        printResult(className, all, successCount, errorCount);
    }

    private static Map<Class<?>, List<Method>> initTestMap(Method[] declaredMethods) {
        Map<Class<?>, List<Method>> testMap =
                Map.of(
                        Before.class, new ArrayList<>(),
                        Test.class, new ArrayList<>(),
                        After.class, new ArrayList<>());
        for (Method method : declaredMethods) {
            var annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Before) {
                    testMap.get(Before.class).add(method);
                } else if (annotation instanceof Test) {
                    testMap.get(Test.class).add(method);
                } else if (annotation instanceof After) {
                    testMap.get(After.class).add(method);
                }
            }
        }
        return testMap;
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
