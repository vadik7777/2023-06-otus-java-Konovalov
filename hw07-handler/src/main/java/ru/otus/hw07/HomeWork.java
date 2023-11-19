package ru.otus.hw07;

import java.time.LocalDateTime;
import java.util.List;
import ru.otus.hw07.handler.ComplexProcessor;
import ru.otus.hw07.listener.ListenerPrinterConsole;
import ru.otus.hw07.model.Message;
import ru.otus.hw07.model.ObjectForMessage;
import ru.otus.hw07.processor.homework.ProcessorGenerateExceptionOnEvenSecond;
import ru.otus.hw07.processor.homework.ProcessorSwapFields11AndField12;

@SuppressWarnings("java:S106")
public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        var processors =
                List.of(
                        new ProcessorSwapFields11AndField12(),
                        new ProcessorGenerateExceptionOnEvenSecond(
                                () -> LocalDateTime.now().getSecond()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message =
                new Message.Builder(1L)
                        .field11("field11")
                        .field12("field12")
                        .field13(new ObjectForMessage())
                        .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
