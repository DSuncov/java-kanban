package manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    private final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("tasks-test.csv"));

    public static Stream<Arguments> getTaskAndEpicFromString() {
        return Stream.of(
                Arguments.of("1,TASK,Task1,NEW,Description task1", new Task("Task1", "Description task1", Status.NEW), 1),
                Arguments.of("3,TASK,Task3,IN_PROGRESS,Description task3", new Task("Task3", "Description task3", Status.IN_PROGRESS), 3),
                Arguments.of("5,TASK,Task5,NEW,Description task5", new Task("Task5", "Description task5", Status.NEW), 5),
                Arguments.of("8,EPIC,Epic8,DONE,Description epic8", new Epic("Epic8", "Description epic8", Status.DONE), 8),
                Arguments.of("9,TASK,Task9,DONE,Description task9", new Task("Task9", "Description task9", Status.DONE), 9),
                Arguments.of("12,TASK,Task12,IN_PROGRESS,Description task12", new Task("Task12", "Description task12", Status.IN_PROGRESS), 12),
                Arguments.of("13,EPIC,Epic13,NEW,Description epic13", new Epic("Epic13", "Description epic13", Status.NEW), 13),
                Arguments.of("16,TASK,Task16,IN_PROGRESS,Description task16", new Task("Task16", "Description task16", Status.IN_PROGRESS), 16),
                Arguments.of("18,EPIC,Epic18,NEW,Description epic18", new Epic("Epic18", "Description epic18", Status.NEW), 18),
                Arguments.of("25,EPIC,Epic25,DONE,Description epic25", new Epic("Epic25", "Description epic25", Status.DONE), 25)
                );
    }

    public static Stream<Arguments> getSubtaskFromString() {
        return Stream.of(
                Arguments.of("1,SUBTASK,Subtask1,NEW,Description subtask1,10", new Subtask("Subtask1", "Description subtask1", Status.NEW), 1, 10),
                Arguments.of("2,SUBTASK,Subtask2,NEW,Description subtask2,10", new Subtask("Subtask2", "Description subtask2", Status.NEW), 2, 10),
                Arguments.of("3,SUBTASK,Subtask3,NEW,Description subtask3,12", new Subtask("Subtask3", "Description subtask3", Status.NEW), 3, 12),
                Arguments.of("4,SUBTASK,Subtask4,NEW,Description subtask4,11", new Subtask("Subtask4", "Description subtask4", Status.NEW), 4, 11),
                Arguments.of("5,SUBTASK,Subtask5,NEW,Description subtask5,12", new Subtask("Subtask5", "Description subtask5", Status.NEW), 5, 12),
                Arguments.of("6,SUBTASK,Subtask6,NEW,Description subtask6,11", new Subtask("Subtask6", "Description subtask6", Status.NEW), 6, 11),
                Arguments.of("7,SUBTASK,Subtask7,NEW,Description subtask7,12", new Subtask("Subtask7", "Description subtask7", Status.NEW), 7, 12),
                Arguments.of("8,SUBTASK,Subtask8,NEW,Description subtask8,10", new Subtask("Subtask8", "Description subtask8", Status.NEW), 8, 10),
                Arguments.of("9,SUBTASK,Subtask9,NEW,Description subtask9,12", new Subtask("Subtask9", "Description subtask9", Status.NEW), 9, 12)
        );
    }

    @DisplayName("Проверяет метод fromString")
    @ParameterizedTest
    @MethodSource("getTaskAndEpicFromString")
    public void should_Return_TaskAndEpic_From_String(String value, Task task, int id) {
        Task expected = fileBackedTaskManager.fromString(value);

        Task actual = task;
        actual.setId(id);

        assertEquals(expected, actual);
    }

    @DisplayName("Проверяет метод fromString")
    @ParameterizedTest
    @MethodSource("getSubtaskFromString")
    public void should_Return_Subtask_From_String(String value, Subtask subtask, int id, int epicId) {
        Subtask expected = (Subtask) fileBackedTaskManager.fromString(value);

        subtask.setId(id);
        subtask.setEpicId(epicId);

        assertEquals(expected, subtask);
    }

    @DisplayName("Проверяет метод save() и fromString()")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getTaskById")
    public void should_Return_Equals_Tasks(Task task) throws IOException {
        File file = new File("test.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.createTask(task);

        String result = Files.readString(file.toPath(), StandardCharsets.UTF_8).replace("\r", "");
        String[] elements = result.split("\n");
        String element = elements[1];

        Task taskFromString = manager.fromString(element);

        assertEquals(task, taskFromString);
    }

    @DisplayName("Проверяет метод save() и fromString()")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getEpicById")
    public void should_Return_Equals_Epics(Epic task) throws IOException {
        File file = new File("test.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.createTask(task);

        String result = Files.readString(file.toPath(), StandardCharsets.UTF_8).replace("\r", "");
        String[] elements = result.split("\n");
        String element = elements[1];

        Task taskFromString = manager.fromString(element);

        assertEquals(task, taskFromString);
    }

    @DisplayName("Проверяет метод save() и fromString()")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getSubtaskById")
    public void should_Return_Equals_Subtasks(Subtask task) throws IOException {
        File file = new File("test.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.createTask(task);

        String result = Files.readString(file.toPath(), StandardCharsets.UTF_8).replace("\r", "");
        String[] elements = result.split("\n");
        String element = elements[1];

        Task taskFromString = manager.fromString(element);

        assertEquals(task, taskFromString);
    }
}
