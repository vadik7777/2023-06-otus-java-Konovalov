package ru.otus.hw13.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings({"squid:S1068", "java:S112"})
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        var methodList = Arrays.stream(configClass.getDeclaredMethods()).toList();
        var methodToDiList =
                methodList.stream()
                        .filter(m -> Objects.nonNull(m.getAnnotation(AppComponent.class)))
                        .sorted(
                                Comparator.comparingInt(
                                        m -> m.getAnnotation(AppComponent.class).order()))
                        .toList();
        try {
            var configClassInstant = configClass.getDeclaredConstructor().newInstance();
            methodToDiList.forEach(method -> putAppComponent(method, configClassInstant));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void putAppComponent(Method method, Object configClassInstant) {
        var parameters = Arrays.stream(method.getParameterTypes()).toList();
        List<Object> instances;
        if (!parameters.isEmpty()) {
            instances =
                    parameters.stream()
                            .map(
                                    parameter ->
                                            appComponents.stream()
                                                    .filter(
                                                            appComponent ->
                                                                    appComponentEqualsIgnoreImpl(
                                                                            appComponent,
                                                                            parameter))
                                                    .findFirst()
                                                    .orElseThrow(
                                                            () ->
                                                                    new RuntimeException(
                                                                            String.format(
                                                                                    "Component not initialize - %s",
                                                                                    parameter))))
                            .toList();
        } else {
            instances = new ArrayList<>();
        }
        Object instance;
        try {
            instance = method.invoke(configClassInstant, instances.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var appComponentName = method.getAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(appComponentName)) {
            throw new RuntimeException(
                    String.format("Component has already been added - %s", appComponentName));
        }
        appComponents.add(instance);
        appComponentsByName.put(appComponentName, instance);
    }

    private boolean appComponentEqualsIgnoreImpl(Object appComponent, Class<?> componentClass) {
        var appComponentClass = appComponent.getClass();
        var appComponentInterfaces = Arrays.stream(appComponentClass.getInterfaces()).toList();
        return Objects.equals(appComponentClass, componentClass)
                || appComponentInterfaces.stream().anyMatch(i -> Objects.equals(i, componentClass));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(
                    String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var appComponentCandidates =
                appComponents.stream()
                        .filter(
                                appComponent ->
                                        appComponentEqualsIgnoreImpl(appComponent, componentClass))
                        .toList();
        if (appComponentCandidates.size() == 1) {
            return (C) appComponentCandidates.get(0);
        } else if (appComponentCandidates.isEmpty()) {
            throw new RuntimeException(
                    String.format("Component not found - %s", appComponentCandidates));
        } else {
            throw new RuntimeException(
                    String.format("Duplicate component - %s", appComponentCandidates));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        var appComponentCandidate = (C) appComponentsByName.get(componentName);
        if (Objects.isNull(appComponentCandidate)) {
            throw new RuntimeException(
                    String.format("Component by name not found - %s", componentName));
        }
        return appComponentCandidate;
    }
}
