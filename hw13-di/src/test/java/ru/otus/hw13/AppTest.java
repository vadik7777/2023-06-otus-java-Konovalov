package ru.otus.hw13;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.hw13.appcontainer.api.AppComponent;
import ru.otus.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.hw13.config.AppConfig;
import ru.otus.hw13.services.*;

class AppTest {

    @DisplayName(
            "Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(
            value = {
                "GameProcessor, ru.otus.hw13.services.GameProcessor",
                "GameProcessorImpl, ru.otus.hw13.services.GameProcessor",
                "gameProcessor, ru.otus.hw13.services.GameProcessor",
                "IOService, ru.otus.hw13.services.IOService",
                "IOServiceStreams, ru.otus.hw13.services.IOService",
                "ioService, ru.otus.hw13.services.IOService",
                "PlayerService, ru.otus.hw13.services.PlayerService",
                "PlayerServiceImpl, ru.otus.hw13.services.PlayerService",
                "playerService, ru.otus.hw13.services.PlayerService",
                "EquationPreparer, ru.otus.hw13.services.EquationPreparer",
                "EquationPreparerImpl, ru.otus.hw13.services.EquationPreparer",
                "equationPreparer, ru.otus.hw13.services.EquationPreparer"
            })
    void shouldExtractFromContextCorrectComponentWithNotNullFields(
            String classNameOrBeanId, Class<?> rootClass) throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass =
                    Class.forName("ru.otus.hw13.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields =
                Arrays.stream(component.getClass().getDeclaredFields())
                        .filter(f -> !Modifier.isStatic(f.getModifiers()))
                        .peek(f -> f.setAccessible(true))
                        .collect(Collectors.toList());

        for (var field : fields) {
            var fieldValue = field.get(component);
            assertThat(fieldValue)
                    .isNotNull()
                    .isInstanceOfAny(
                            IOService.class,
                            PlayerService.class,
                            EquationPreparer.class,
                            PrintStream.class,
                            Scanner.class);
        }
    }

    @DisplayName("В контексте не должно быть компонентов с одинаковым именем")
    @Test
    void shouldNotAllowTwoComponentsWithSameName() {
        assertThatCode(
                        () ->
                                new AppComponentsContainerImpl(
                                        ConfigWithTwoComponentsWithSameName.class))
                .isInstanceOf(Exception.class);
    }

    @DisplayName(
            "При попытке достать из контекста отсутствующий или дублирующийся компонент, должно выкидываться исключение")
    @Test
    void shouldThrowExceptionWhenContainerContainsMoreThanOneOrNoneExpectedComponents() {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatCode(() -> ctx.getAppComponent(EquationPreparer.class))
                .isInstanceOf(Exception.class);

        assertThatCode(() -> ctx.getAppComponent(PlayerService.class))
                .isInstanceOf(Exception.class);

        assertThatCode(() -> ctx.getAppComponent("equationPreparer3"))
                .isInstanceOf(Exception.class);
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoComponentsWithSameName {
        public ConfigWithTwoComponentsWithSameName() {}

        @AppComponent(order = 1, name = "equationPreparer")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer")
        public IOService ioService() {
            return new IOServiceStreams(System.out, System.in);
        }
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoSameComponents {

        @AppComponent(order = 1, name = "equationPreparer1")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer2")
        public EquationPreparer equationPreparer2() {
            return new EquationPreparerImpl();
        }
    }
}
