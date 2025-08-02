package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static manager.Fields.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static TaskManager taskManager = Managers.getDefault();

    @BeforeAll
    public static void createTasks() {
        taskManager.createTask(new Task(TASK_TITLE[0], TASK_DESCRIPTION[0], DEFAULT_STATUS));
        taskManager.createTask(new Task(TASK_TITLE[1], TASK_DESCRIPTION[1], DEFAULT_STATUS));
        taskManager.createTask(new Task(TASK_TITLE[2], TASK_DESCRIPTION[2], DEFAULT_STATUS));
        taskManager.createTask(new Task(TASK_TITLE[3], TASK_DESCRIPTION[3], DEFAULT_STATUS));

        var epic1 = new Epic(TASK_TITLE[4], TASK_DESCRIPTION[4], DEFAULT_STATUS);
        var epic2 = new Epic(TASK_TITLE[5], TASK_DESCRIPTION[5], DEFAULT_STATUS);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.createSubtask(epic1, new Subtask(SUBTASK_TITLE[0], SUBTASK_DESCRIPTION[0], DEFAULT_STATUS));
        taskManager.createSubtask(epic1, new Subtask(SUBTASK_TITLE[1], SUBTASK_DESCRIPTION[1], DEFAULT_STATUS));
        taskManager.createSubtask(epic1, new Subtask(SUBTASK_TITLE[2], SUBTASK_DESCRIPTION[2], DEFAULT_STATUS));
        taskManager.createSubtask(epic2, new Subtask(SUBTASK_TITLE[3], SUBTASK_DESCRIPTION[3], DEFAULT_STATUS));
        taskManager.createSubtask(epic2, new Subtask(SUBTASK_TITLE[4], SUBTASK_DESCRIPTION[4], DEFAULT_STATUS));
        taskManager.createSubtask(epic2, new Subtask(SUBTASK_TITLE[5], SUBTASK_DESCRIPTION[5], DEFAULT_STATUS));
    }

    @AfterEach
    public void clearList() {
        taskManager.getHistoryManager().getHistory().clear();
    }

    @DisplayName("Проверяет, что метод getHistory() возвращает ожидаемый размер списка, который равен количеству вызовов метода geTask/Epic/Subtask()")
    @Test
    void should_Return5_WhenGetTask5Times_Test() {
        //given
        taskManager.getTask(1);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(6), 2);
        //when
        int historyManagerSizeExpected = 5; // В списке должно находиться 5 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() возвращает ожидаемое количество элементов, равное количеству вызовов метода geTask/Epic/Subtask()")
    @Test
    void should_Return8_WhenGetTask8Times_Test() {
        //given
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(5), 1);
        taskManager.getSubtask(taskManager.getEpic(6), 2);
        //when
        int historyManagerSizeExpected = 8; // В списке должно находиться 5 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() не может возвращать список, размер которого больше 10")
    @Test
    void should_NotEquals_WhenGetTask11Times_Test() {
        //given
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        taskManager.getSubtask(taskManager.getEpic(5), 1);
        taskManager.getSubtask(taskManager.getEpic(5), 2);
        taskManager.getSubtask(taskManager.getEpic(6), 2);
        //when
        int historyManagerSizeExpected = 11;
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size(); // Должно быть 10
        //then
        assertNotEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размер списка не может быть больше 10.");
    }

    @DisplayName("Проверяет, что метод getHistory() возвращает старую и новую версию задачи")
    @Test
    void should_SaveLastAndNewVersion_InHistoryManager_Test() {
        //given
        List<Task> expectedList = new ArrayList<>();
        //when
        Task task = taskManager.getTask(1);
        taskManager.updateTask(1, "Задача № 1", "Версия № 2", Status.IN_PROGRESS);
        Task updateTask = taskManager.getTask(1);
        expectedList.add(task);
        expectedList.add(updateTask);
        //then
        assertEquals(2, taskManager.getHistoryManager().getHistory().size());
        assertEquals(expectedList, taskManager.getHistoryManager().getHistory());
    }
}