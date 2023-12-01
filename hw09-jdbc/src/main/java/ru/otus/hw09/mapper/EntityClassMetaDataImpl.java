package ru.otus.hw09.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("java:S112")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    private final Class<T> clazz;

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            var fieldClasses = getAllFields().stream().map(Field::getType).toArray(Class<?>[]::new);
            return clazz.getDeclaredConstructor(fieldClasses);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        var fields = getAllFields();
        return fields.stream()
                .filter(field -> Objects.nonNull(field.getAnnotation(Id.class)))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        var fields = getAllFields();
        return fields.stream()
                .filter(field -> Objects.isNull(field.getAnnotation(Id.class)))
                .toList();
    }
}
