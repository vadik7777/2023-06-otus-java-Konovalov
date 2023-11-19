package ru.otus.hw07.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectForMessage implements Copyable<ObjectForMessage> {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        var copy = new ObjectForMessage();
        if (Objects.nonNull(data)) {
            copy.setData(new ArrayList<>(data));
        }
        return copy;
    }
}
