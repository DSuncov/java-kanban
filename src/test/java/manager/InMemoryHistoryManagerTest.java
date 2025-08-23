package manager;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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

    @BeforeEach
    public void clearHistoryList() {
        taskManager.getHistoryManager().removeAll();
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (5)")
    @Test
    void should_Return5_WhenGetTask5Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(4); // 2
        taskManager.getEpic(5); // 3
        taskManager.getSubtask(taskManager.getEpic(6), 1); // 4 и 5
        //when
        int historyManagerSizeExpected = 5; // В списке должно находиться 5 элементов
        List<Task> listExpected = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = listExpected.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (7)")
    @Test
    void should_Return8_WhenGetTask8Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(2); // 2
        taskManager.getTask(4); // 3
        taskManager.getEpic(5); // Должна быть удалена
        taskManager.getSubtask(taskManager.getEpic(5), 1); // 4 и 5
        taskManager.getSubtask(taskManager.getEpic(6), 2); // 6 и 7
        //when
        int historyManagerSizeExpected = 7; // В списке должно находиться 7 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (8)")
    @Test
    void should_NotEquals_WhenGetTask11Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(2); // 2
        taskManager.getTask(3); // 3
        taskManager.getTask(4); // 4
        taskManager.getEpic(5); // Должна быть удалена
        taskManager.getSubtask(taskManager.getEpic(5), 1); // 5
        taskManager.getSubtask(taskManager.getEpic(5), 2); // 6 и 7
        taskManager.getSubtask(taskManager.getEpic(6), 2); // 8
        //when
        int historyManagerSizeExpected = 8;
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size(); // Должно быть 8
        //then
        assertNotEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размер отличаются");
    }
}