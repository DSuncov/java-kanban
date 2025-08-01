package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static TaskManager taskManager = Managers.getDefault();

    @BeforeAll
    public static void createTasks() {
        taskManager.createTask(new Task("Задача № 1", "Описание № 1", Status.NEW));
        taskManager.createTask(new Task("Задача № 2", "Описание № 2", Status.NEW));
        taskManager.createTask(new Task("Задача № 3", "Описание № 3", Status.NEW));
        taskManager.createTask(new Task("Задача № 4", "Описание № 4", Status.NEW));

        var epic1 = new Epic("Задача № 5", "Описание № 5", Status.NEW);
        var epic2 = new Epic("Задача № 6", "Описание № 6", Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.createSubtask(epic1, new Subtask("Подзадача № 1.1", "Описание № 1.1", Status.NEW));
        taskManager.createSubtask(epic1, new Subtask("Подзадача № 1.2", "Описание № 1.2", Status.NEW));
        taskManager.createSubtask(epic1, new Subtask("Подзадача № 1.3", "Описание № 1.3", Status.NEW));
        taskManager.createSubtask(epic2, new Subtask("Подзадача № 2.1", "Описание № 2.1", Status.NEW));
        taskManager.createSubtask(epic2, new Subtask("Подзадача № 2.2", "Описание № 2.2", Status.NEW));
        taskManager.createSubtask(epic2, new Subtask("Подзадача № 2.3", "Описание № 2.3", Status.NEW));
    }

    @AfterEach
    public void clearList() {
        taskManager.getHistoryManager().getHistory().clear();
    }

    @Test
    void shouldReturn5WhenGetTask5() {
        taskManager.getTask(1);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(6), 2);

        int historyManagerSizeExpected = 5; // В списке должно находиться 5 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();

        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @Test
    void shouldReturn8WhenGetTask8() {
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(5), 1);
        taskManager.getSubtask(taskManager.getEpic(6), 2);

        int historyManagerSizeExpected = 8; // В списке должно находиться 5 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();

        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @Test
    void shouldNotEqualsWhenGetTask11() {
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(5), 1);
        taskManager.getSubtask(taskManager.getEpic(5), 2);
        taskManager.getSubtask(taskManager.getEpic(6), 2);

        int historyManagerSizeExpected = 11;
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size(); // Должно быть 10

        assertNotEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размер списка не может быть больше 10.");
    }

    @Test
    void shouldSaveLastVersionInHistoryManager() {
        List<Task> expectedList = new ArrayList<>();
        Task task = taskManager.getTask(1);
        taskManager.updateTask(1, "Задача № 1", "Версия № 2", Status.IN_PROGRESS);
        Task updateTask = taskManager.getTask(1);
        expectedList.add(task);
        expectedList.add(updateTask);

        assertEquals(2, taskManager.getHistoryManager().getHistory().size());
        assertEquals(expectedList, taskManager.getHistoryManager().getHistory());
    }

}