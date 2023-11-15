package ru.hw08.dataprocessor;

import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

@SuppressWarnings("java:S112")
public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        var gson = new Gson();
        try (var outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName))) {
            gson.toJson(data, outputStreamWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
