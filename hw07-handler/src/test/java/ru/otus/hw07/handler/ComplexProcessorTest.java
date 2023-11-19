package ru.otus.hw07.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw07.listener.Listener;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.processor.Processor;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        // given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = Mockito.mock(Processor.class);
        Mockito.when(processor1.process(message)).thenReturn(message);

        var processor2 = Mockito.mock(Processor.class);
        Mockito.when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        // when
        var result = complexProcessor.handle(message);

        // then
        Mockito.verify(processor1).process(message);
        Mockito.verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = Mockito.mock(Processor.class);
        Mockito.when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = Mockito.mock(Processor.class);
        Mockito.when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor =
                new ComplexProcessor(
                        processors,
                        (ex) -> {
                            throw new TestException(ex.getMessage());
                        });

        // when
        Assertions.assertThatExceptionOfType(TestException.class)
                .isThrownBy(() -> complexProcessor.handle(message));

        // then
        Mockito.verify(processor1, Mockito.times(1)).process(message);
        Mockito.verify(processor2, Mockito.never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        // given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = Mockito.mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {});

        complexProcessor.addListener(listener);

        // when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        // then
        Mockito.verify(listener, Mockito.times(1)).onUpdated(message);
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}
