package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskEpicSubtaskTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static TaskManager taskManager = Managers.getDefault();

    public static Stream<Arguments> createTask() {
        return Stream.of(
                Arguments.of(new Task("Задача № 1", "Описание задачи № 1", Status.NEW, "2025-09-22 09:00", 50L), "2025-09-22 09:00", "2025-09-22 09:50"),
                Arguments.of(new Task("Задача № 2", "Описание задачи № 2", Status.NEW, "2025-09-22 13:10", 40L), "2025-09-22 13:10", "2025-09-22 13:50"),
                Arguments.of(new Task("Задача № 3", "Описание задачи № 3", Status.NEW, "2025-09-22 15:30", 120L), "2025-09-22 15:30", "2025-09-22 17:30"),
                Arguments.of(new Task("Задача № 4", "Описание задачи № 4", Status.NEW, "2025-09-23 10:15", 300L), "2025-09-23 10:15", "2025-09-23 15:15")
            );
    }

    @BeforeAll
    public static void createEpicWithSubtask() {
        Epic epic1 = new Epic("Эпик № 1", "Описание эпика № 1", Status.NEW);
        Epic epic2 = new Epic("Эпик № 2", "Описание эпика № 2", Status.NEW);
        Epic epic3 = new Epic("Эпик № 3", "Описание эпика № 3", Status.NEW);
        Epic epic4 = new Epic("Эпик № 4", "Описание эпика № 4", Status.NEW);
        Epic epic5 = new Epic("Эпик № 5", "Описание эпика № 5", Status.NEW);
        Epic epic6 = new Epic("Эпик № 6", "Описание эпика № 6", Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createEpic(epic4);
        taskManager.createEpic(epic5);
        taskManager.createEpic(epic6);

        Subtask subtask1 = new Subtask("Подзадача № 1 эпика № 1", "Описание подзадачи № 1 эпика № 1", Status.NEW, "2025-09-23 09:00", 15L);
        Subtask subtask2 = new Subtask("Подзадача № 2 эпика № 1", "Описание подзадачи № 2 эпика № 1", Status.NEW, "2025-09-23 10:00", 20L);
        Subtask subtask3 = new Subtask("Подзадача № 3 эпика № 1", "Описание подзадачи № 3 эпика № 1", Status.NEW, "2025-09-23 11:00", 25L);
        Subtask subtask4 = new Subtask("Подзадача № 1 эпика № 2", "Описание подзадачи № 1 эпика № 2", Status.NEW, "2025-09-24 09:00", 25L);
        Subtask subtask5 = new Subtask("Подзадача № 2 эпика № 2", "Описание подзадачи № 2 эпика № 2", Status.NEW, "2025-09-24 10:00", 30L);
        Subtask subtask6 = new Subtask("Подзадача № 3 эпика № 2", "Описание подзадачи № 3 эпика № 2", Status.NEW, "2025-09-24 11:00", 10L);

        Subtask subtask7 = new Subtask("Подзадача № 1 эпика № 4", "Описание подзадачи № 1 эпика № 4", Status.DONE, "2025-09-26 09:00", 25L);
        Subtask subtask8 = new Subtask("Подзадача № 2 эпика № 4", "Описание подзадачи № 2 эпика № 4", Status.DONE, "2025-09-26 10:00", 30L);
        Subtask subtask9 = new Subtask("Подзадача № 3 эпика № 4", "Описание подзадачи № 3 эпика № 4", Status.DONE, "2025-09-26 11:00", 10L);

        Subtask subtask10 = new Subtask("Подзадача № 1 эпика № 4", "Описание подзадачи № 1 эпика № 4", Status.NEW, "2025-09-27 09:00", 25L);
        Subtask subtask11 = new Subtask("Подзадача № 2 эпика № 4", "Описание подзадачи № 2 эпика № 4", Status.NEW, "2025-09-27 10:00", 30L);
        Subtask subtask12 = new Subtask("Подзадача № 3 эпика № 4", "Описание подзадачи № 3 эпика № 4", Status.DONE, "2025-09-27 11:00", 10L);

        Subtask subtask13 = new Subtask("Подзадача № 1 эпика № 4", "Описание подзадачи № 1 эпика № 4", Status.IN_PROGRESS, "2025-09-28 09:00", 25L);
        Subtask subtask14 = new Subtask("Подзадача № 2 эпика № 4", "Описание подзадачи № 2 эпика № 4", Status.IN_PROGRESS, "2025-09-28 10:00", 30L);
        Subtask subtask15 = new Subtask("Подзадача № 3 эпика № 4", "Описание подзадачи № 3 эпика № 4", Status.IN_PROGRESS, "2025-09-28 11:00", 10L);

        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createSubtask(epic1, subtask3);
        taskManager.createSubtask(epic2, subtask4);
        taskManager.createSubtask(epic2, subtask5);
        taskManager.createSubtask(epic2, subtask6);
        taskManager.createSubtask(epic4, subtask7);
        taskManager.createSubtask(epic4, subtask8);
        taskManager.createSubtask(epic4, subtask9);
        taskManager.createSubtask(epic5, subtask10);
        taskManager.createSubtask(epic5, subtask11);
        taskManager.createSubtask(epic5, subtask12);
        taskManager.createSubtask(epic6, subtask13);
        taskManager.createSubtask(epic6, subtask14);
        taskManager.createSubtask(epic6, subtask15);

    }

    @DisplayName("Проверяет метод getStartTime")
    @ParameterizedTest
    @MethodSource("createTask")
    public void should_Return_Correct_StartTime_Of_Task_Test(Task task, String startTime) {
        LocalDateTime expectedStartTime = LocalDateTime.parse(startTime, formatter);

        assertEquals(task.getStartTime(), expectedStartTime);
    }

    @DisplayName("Проверяет метод getEndTime")
    @ParameterizedTest
    @MethodSource("createTask")
    public void should_Return_Correct_EndTime_Of_Task_Test(Task task, String startTime, String endTime) {
        LocalDateTime expectedEndTime = LocalDateTime.parse(endTime, formatter);

        assertEquals(task.getEndTime(), expectedEndTime);
    }

    @DisplayName("Проверяет метод getStartTime у эпиков")
    @Test
    public void should_Return_Correct_StartTime_of_Epic_Test() {
        LocalDateTime actualStartTime1 = taskManager.getEpic(1).getStartTime();
        LocalDateTime expectedStartTime1 = LocalDateTime.parse("2025-09-23 09:00", formatter);

        LocalDateTime actualStartTime2 = taskManager.getEpic(2).getStartTime();
        LocalDateTime expectedStartTime2 = LocalDateTime.parse("2025-09-24 09:00", formatter);

        LocalDateTime actualStartTime3 = taskManager.getEpic(3).getStartTime();
        LocalDateTime expectedStartTime3 = LocalDateTime.parse("1970-01-01 00:00", formatter);

        assertEquals(actualStartTime1, expectedStartTime1);
        assertEquals(actualStartTime2, expectedStartTime2);
        assertEquals(actualStartTime3, expectedStartTime3);
    }

    @DisplayName("Проверяет метод getEndTime у эпиков")
    @Test
    public void should_Return_Correct_EndTime_of_Epic_Test() {
        LocalDateTime actualEndTime1 = taskManager.getEpic(1).getEndTime();
        LocalDateTime expectedEndTime1 = LocalDateTime.parse("2025-09-23 11:25", formatter);

        LocalDateTime actualEndTime2 = taskManager.getEpic(2).getEndTime();
        LocalDateTime expectedEndTime2 = LocalDateTime.parse("2025-09-24 11:10", formatter);

        LocalDateTime actualEndTime3 = taskManager.getEpic(3).getEndTime();
        LocalDateTime expectedEndTime3 = LocalDateTime.parse("1970-01-01 00:00", formatter);

        assertEquals(actualEndTime1, expectedEndTime1);
        assertEquals(actualEndTime2, expectedEndTime2);
        assertEquals(actualEndTime3, expectedEndTime3);
    }

    @DisplayName("Проверяет метод getDuration у эпиков")
    @Test
    public void should_Return_Correct_Duration_of_Epic_Test() {
        long actualDuration1 = taskManager.getEpic(1).getDuration();
        long expectedDuration1 = 60;

        long actualDuration2 = taskManager.getEpic(2).getDuration();
        long expectedDuration2 = 65;

        long actualDuration3 = taskManager.getEpic(3).getDuration();
        long expectedDuration3 = 0;

        assertEquals(actualDuration1, expectedDuration1);
        assertEquals(actualDuration2, expectedDuration2);
        assertEquals(actualDuration3, expectedDuration3);
    }

    @DisplayName("Проверяет статус эпиков, если все подзадачи со статусом NEW")
    @Test
    public void should_Return_New_Status_Test() {
        Status actualStatus1 = taskManager.getEpic(1).getStatus();
        Status expectedStatus1 = Status.NEW;

        Status actualStatus2 = taskManager.getEpic(2).getStatus();
        Status expectedStatus2 = Status.NEW;

        assertEquals(actualStatus1, expectedStatus1);
        assertEquals(actualStatus2, expectedStatus2);
    }

    @DisplayName("Проверяет статус эпиков, если все подзадачи со статусом DONE")
    @Test
    public void should_Return_Done_Status_Test() {
        Status actualStatus = taskManager.getEpic(4).getStatus();
        Status expectedStatus = Status.DONE;

        assertEquals(actualStatus, expectedStatus);
    }

    @DisplayName("Проверяет статус эпиков, если все подзадачи имеют статус DONE и NEW")
    @Test
    public void should_Return_InProgress_Status_Test() {
        Status actualStatus = taskManager.getEpic(5).getStatus();
        Status expectedStatus = Status.IN_PROGRESS;

        assertEquals(actualStatus, expectedStatus);
    }

    @DisplayName("Проверяет статус эпиков, если все подзадачи имеют статус DONE и NEW")
    @Test
    public void should_Return_InProgress_Status_Test2() {
        Status actualStatus = taskManager.getEpic(6).getStatus();
        Status expectedStatus = Status.IN_PROGRESS;

        assertEquals(actualStatus, expectedStatus);
    }
}
