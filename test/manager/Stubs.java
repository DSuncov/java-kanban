package manager;

import org.junit.jupiter.params.provider.Arguments;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.stream.Stream;

import static manager.InMemoryTaskManagerTest.taskManager;

public class Stubs {
    public static Stream<Arguments> getTaskById() {
        return Stream.of(
                Arguments.of(new Task("Задача № 1", "Описание № 1", Status.NEW), 1),
                Arguments.of(new Task("Задача № 2", "Описание № 2", Status.NEW), 2),
                Arguments.of(new Task("Задача № 3", "Описание № 3", Status.NEW), 3),
                Arguments.of(new Task("Задача № 4", "Описание № 4", Status.NEW), 4)
        );
    }

    public static Stream<Arguments> getEpicById() {
        return Stream.of(
                Arguments.of(new Epic("Задача № 5", "Описание № 5", Status.NEW), 5),
                Arguments.of(new Epic("Задача № 6", "Описание № 6", Status.NEW), 6)
        );
    }

    public static Stream<Arguments> getSubtaskById() {
        return Stream.of(
                Arguments.of(new Subtask("Подзадача № 1.1", "Описание № 1.1", Status.NEW), taskManager.getEpic(5), 1),
                Arguments.of(new Subtask("Подзадача № 1.2", "Описание № 1.2", Status.NEW), taskManager.getEpic(5), 2),
                Arguments.of(new Subtask("Подзадача № 1.3", "Описание № 1.3", Status.NEW), taskManager.getEpic(5), 3),
                Arguments.of(new Subtask("Подзадача № 2.1", "Описание № 2.1", Status.NEW), taskManager.getEpic(6), 1),
                Arguments.of(new Subtask("Подзадача № 2.2", "Описание № 2.2", Status.NEW), taskManager.getEpic(6), 2),
                Arguments.of(new Subtask("Подзадача № 2.3", "Описание № 2.3", Status.NEW), taskManager.getEpic(6), 3)
        );
    }
}
