package ru.otus.hw07.processor.homework;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.model.Message;

@DisplayName("Процессор для генерации ошибки на четной секунде должен")
class ProcessorGenerateExceptionOnEvenSecondTest {

    @DisplayName("генерировать ошибку")
    @Test
    void shouldCorrectGenerateExceptionOnEvenSecond() {
        var processor = new ProcessorGenerateExceptionOnEvenSecond(() -> 2);
        var message = new Message.Builder(1L).build();
        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @DisplayName("не генерировать ошибку")
    @Test
    void shouldCorrectProcess() {
        var processor = new ProcessorGenerateExceptionOnEvenSecond(() -> 1);
        var expectedMessage = new Message.Builder(1L).build();
        var actualMessage = processor.process(expectedMessage);
        assertEquals(expectedMessage, actualMessage);
    }
}
