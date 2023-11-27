package ru.otus.hw09.mapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.otus.hw09.legacy.core.repository.DataTemplate;
import ru.otus.hw09.legacy.core.repository.DataTemplateException;
import ru.otus.hw09.legacy.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings({"java:S1068", "java:S3011"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                rs -> {
                    try {
                        if (rs.next()) {
                            var allFields = entityClassMetaData.getAllFields();
                            var args = new Object[allFields.size()];
                            for (int idx = 0; idx < allFields.size(); idx++) {
                                var fieldName = allFields.get(idx).getName();
                                var obj = rs.getObject(fieldName);
                                args[idx] = obj;
                            }
                            return entityClassMetaData.getConstructor().newInstance(args);
                        }
                        return null;
                    } catch (SQLException
                            | InstantiationException
                            | IllegalAccessException
                            | InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(Connection connection, T client) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        try {
            var values = new ArrayList<>();
            for (var field : fieldsWithoutId) {
                field.setAccessible(true);
                var value = field.get(client);
                values.add(value);
            }
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        throw new UnsupportedOperationException();
    }
}
