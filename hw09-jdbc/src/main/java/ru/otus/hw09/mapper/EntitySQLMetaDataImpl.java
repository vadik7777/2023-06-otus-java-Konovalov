package ru.otus.hw09.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSelectByIdSql() {
        var entityName = entityClassMetaData.getName();
        var allFields = entityClassMetaData.getAllFields();
        var idField = entityClassMetaData.getIdField().getName();
        var allFieldsString =
                allFields.stream().map(Field::getName).collect(Collectors.joining(","));
        var stringBuilder = new StringBuilder();
        stringBuilder.append("select ");
        stringBuilder.append(allFieldsString);
        stringBuilder.append(" from ");
        stringBuilder.append(entityName);
        stringBuilder.append(" where ");
        stringBuilder.append(idField);
        stringBuilder.append(" = ?");
        return stringBuilder.toString();
    }

    @Override
    public String getInsertSql() {
        var entityName = entityClassMetaData.getName();
        var fieldsWithOutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsWithOutIdSql =
                fieldsWithOutId.stream().map(Field::getName).collect(Collectors.joining(","));
        var stringBuilder = new StringBuilder();
        stringBuilder.append("insert into ");
        stringBuilder.append(entityName);
        stringBuilder.append("(");
        stringBuilder.append(fieldsWithOutIdSql);
        stringBuilder.append(") values (");
        int retryQuestions = fieldsWithOutId.size();
        for (int i = 0; retryQuestions > i; i++) {
            stringBuilder.append("?");
            if (i != retryQuestions - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        throw new UnsupportedOperationException();
    }
}
