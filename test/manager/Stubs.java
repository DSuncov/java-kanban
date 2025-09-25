package manager;

import org.junit.jupiter.params.provider.Arguments;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.stream.Stream;

public class Stubs {
    public static Stream<Arguments> getTaskById() {
        return Stream.of(
                Arguments.of(new Task("Задача № 1", "Описание № 1", Status.NEW, "2025-10-01 09:00", 10L), 1),
                Arguments.of(new Task("Задача № 2", "Описание № 2", Status.NEW, "2025-10-02 09:00", 10L), 2),
                Arguments.of(new Task("Задача № 3", "Описание № 3", Status.NEW, "2025-10-03 09:00", 10L), 3),
                Arguments.of(new Task("Задача № 4", "Описание № 4", Status.NEW, "2025-10-04 09:00", 10L), 4)
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
                Arguments.of(new Subtask("Подзадача № 1.1", "Описание № 1.1", Status.NEW, "2025-09-22 14:00", 20L), 5, 7),
                Arguments.of(new Subtask("Подзадача № 1.2", "Описание № 1.2", Status.NEW, "2025-09-22 14:20", 10L), 5, 8),
                Arguments.of(new Subtask("Подзадача № 1.3", "Описание № 1.3", Status.NEW, "2025-09-22 14:40", 15L), 5, 9),
                Arguments.of(new Subtask("Подзадача № 2.1", "Описание № 2.1", Status.NEW, "2025-09-22 15:00", 5L), 6, 10),
                Arguments.of(new Subtask("Подзадача № 2.2", "Описание № 2.2", Status.NEW, "2025-09-22 15:30", 25L), 6, 11),
                Arguments.of(new Subtask("Подзадача № 2.3", "Описание № 2.3", Status.NEW, "2025-09-22 16:00", 30L), 6, 12)
        );
    }
}
