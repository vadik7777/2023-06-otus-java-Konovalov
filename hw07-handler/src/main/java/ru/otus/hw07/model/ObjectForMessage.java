package ru.otus.hw07.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ObjectForMessage {

    public ObjectForMessage() {}

    public ObjectForMessage(ObjectForMessage objectForMessage) {
        var objectForMessageData =
                Optional.ofNullable(objectForMessage).map(ObjectForMessage::getData).orElse(null);
        if (!Objects.isNull(objectForMessageData)) {
            this.data = new ArrayList<>(objectForMessageData);
        }
    }

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
