package ru.otus.hw07.processor.homework;

import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

@SuppressWarnings("java:S112")
public class ProcessorGenerateExceptionOnEvenSecond implements Processor {

    public ProcessorGenerateExceptionOnEvenSecond(SecondsProvider secondsProvider) {
        this.secondsProvider = secondsProvider;
    }

    private final SecondsProvider secondsProvider;

    @Override
    public Message process(Message message) {
        var second = secondsProvider.getCurrentSeconds();
        if (second % 2 == 0) {
            throw new RuntimeException();
        }
        return message.toBuilder().build();
    }
}
