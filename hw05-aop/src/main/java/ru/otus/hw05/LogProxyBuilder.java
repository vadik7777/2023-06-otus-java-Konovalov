package ru.otus.hw05;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"java:S106", "java:S112"})
class LogProxyBuilder {

    private LogProxyBuilder() {}

    static Object createLogProxy(Class<?> implClass) {
        var declaredMethods = implClass.getDeclaredMethods();
        List<Method> logMethod = new ArrayList<>();
        for (Method method : declaredMethods) {
            var annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Log) {
                    logMethod.add(getInterfaceMethod(implClass, method));
                }
            }
        }
        var implObject = instantiate(implClass);
        if (logMethod.isEmpty()) {
            return implObject;
        }
        var handler = new LogHandler(implObject, logMethod);
        return Proxy.newProxyInstance(
                LogProxyBuilder.class.getClassLoader(),
                new Class<?>[] {InterfaceClass.class},
                handler);
    }

    private static boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i]) return false;
            }
            return true;
        }
        return false;
    }

    static class LogHandler implements InvocationHandler {
        private final Object implObject;
        private final List<Method> logMethods;

        LogHandler(Object implObject, List<Method> logMethods) {
            this.implObject = implObject;
            this.logMethods = logMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(method)) {
                var argsString =
                        Stream.of(args).map(Object::toString).collect(Collectors.joining(", "));
                System.out.println(
                        "executed method: " + method.getName() + ", param(s): " + argsString);
            }
            return method.invoke(implObject, args);
        }
    }

    private static Method getInterfaceMethod(Class<?> implClass, Method implMethod) {
        var interfaces = implClass.getInterfaces();
        var interfaceMethods =
                Arrays.stream(interfaces)
                        .flatMap(methods -> Arrays.stream(methods.getMethods()))
                        .toList();
        return interfaceMethods.stream()
                .filter(
                        interfaceMethod ->
                                Objects.equals(interfaceMethod.getName(), implMethod.getName()))
                .filter(
                        interfaceMethod ->
                                Objects.equals(
                                        interfaceMethod.getReturnType(),
                                        implMethod.getReturnType()))
                .filter(
                        interfaceMethod ->
                                equalParamTypes(
                                        interfaceMethod.getParameterTypes(),
                                        implMethod.getParameterTypes()))
                .findFirst()
                .orElseThrow();
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
