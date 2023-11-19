package ru.otus.hw07.processor.homework;

import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

public class ProcessorSwapFields11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        var newField11 = message.getField12();
        var newField12 = message.getField11();
        return message.toBuilder().field11(newField11).field12(newField12).build();
    }
}
