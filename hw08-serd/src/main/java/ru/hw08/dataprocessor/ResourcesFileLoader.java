package ru.hw08.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import ru.hw08.model.Measurement;

@SuppressWarnings("java:S112")
public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        var classLoader = this.getClass().getClassLoader();
        var gson = new Gson();
        var measurementListType = new TypeToken<ArrayList<Measurement>>() {}.getType();
        try (var reader = new InputStreamReader(classLoader.getResourceAsStream(fileName))) {
            return gson.fromJson(reader, measurementListType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
