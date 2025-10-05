package manager;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static manager.Fields.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryHistoryManagerTest {

    static HistoryManager historyManager = Managers.getDefaultHistory();
    static TaskManager taskManager = Managers.getDefault();

    static List<Task> expected = new ArrayList<>();
    static Task task1 = new Task(TASK_TITLE[0], TASK_DESCRIPTION[0], DEFAULT_STATUS, TASKDATETIME[0], TASKDURATION[0]);
    static Task task2 = new Task(TASK_TITLE[1], TASK_DESCRIPTION[1], DEFAULT_STATUS, TASKDATETIME[1], TASKDURATION[1]);
    static Task task3 = new Task(TASK_TITLE[2], TASK_DESCRIPTION[2], DEFAULT_STATUS, TASKDATETIME[2], TASKDURATION[2]);
    static Task task4 = new Task(TASK_TITLE[3], TASK_DESCRIPTION[3], DEFAULT_STATUS, TASKDATETIME[3], TASKDURATION[3]);
    static Epic epic1 = new Epic(TASK_TITLE[4], TASK_DESCRIPTION[4], DEFAULT_STATUS);
    static Epic epic2 = new Epic(TASK_TITLE[5], TASK_DESCRIPTION[5], DEFAULT_STATUS);
    static Subtask subtask1 = new Subtask(SUBTASK_TITLE[0], SUBTASK_DESCRIPTION[0], DEFAULT_STATUS, SUBTASKDATETIME[0], SUBTASKDURATION[0]);
    static Subtask subtask2 = new Subtask(SUBTASK_TITLE[1], SUBTASK_DESCRIPTION[1], DEFAULT_STATUS, SUBTASKDATETIME[1], SUBTASKDURATION[1]);
    static Subtask subtask3 = new Subtask(SUBTASK_TITLE[2], SUBTASK_DESCRIPTION[2], DEFAULT_STATUS, SUBTASKDATETIME[2], SUBTASKDURATION[2]);
    static Subtask subtask4 = new Subtask(SUBTASK_TITLE[3], SUBTASK_DESCRIPTION[3], DEFAULT_STATUS, SUBTASKDATETIME[3], SUBTASKDURATION[3]);
    static Subtask subtask5 = new Subtask(SUBTASK_TITLE[4], SUBTASK_DESCRIPTION[4], DEFAULT_STATUS, SUBTASKDATETIME[4], SUBTASKDURATION[4]);
    static Subtask subtask6 = new Subtask(SUBTASK_TITLE[5], SUBTASK_DESCRIPTION[5], DEFAULT_STATUS, SUBTASKDATETIME[5], SUBTASKDURATION[5]);

    @BeforeEach
    public void createTask() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createSubtask(epic1, subtask3);
        taskManager.createSubtask(epic2, subtask4);
        taskManager.createSubtask(epic2, subtask5);
        taskManager.createSubtask(epic2, subtask6);
    }

    @AfterEach
    public void clearList() {
        expected.clear();
        taskManager.getHistoryManager().removeAll();
    }

    @AfterEach
    public void clearHistoryList() {
        taskManager.getHistoryManager().removeAll();
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (5)")
    @Test
    void inMemoryHistoryManager_getHistory_shouldReturn5_WhenGetTask5Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(4); // 2
        taskManager.getEpic(5); // 3
        taskManager.getSubtask(taskManager.getEpic(6), 10); // 4 и 5
        //when
        int historyManagerSizeExpected = 5; // В списке должно находиться 5 элементов
        List<Task> listExpected = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = listExpected.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (7)")
    @Test
    void inMemoryHistoryManager_getHistory_shouldReturn7_WhenGetTask8Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(2); // 2
        taskManager.getTask(4); // 3
        taskManager.getEpic(5); // Должна быть удалена
        taskManager.getSubtask(taskManager.getEpic(5), 7); // 4 и 5
        taskManager.getSubtask(taskManager.getEpic(6), 10); // 6 и 7
        //when
        int historyManagerSizeExpected = 7; // В списке должно находиться 7 элементов
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size();
        //then
        assertEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размеры отличаются.");
    }

    @DisplayName("Проверяет, что метод getHistory() вовзвращает список ожидаемого размера (8)")
    @Test
    void inMemoryHistoryManager_getHistory_shouldNotEquals_WhenGetTask11Times_Test() {
        //given
        taskManager.getTask(1); // 1
        taskManager.getTask(2); // 2
        taskManager.getTask(3); // 3
        taskManager.getTask(4); // 4
        taskManager.getEpic(5); // Должна быть удалена
        taskManager.getSubtask(taskManager.getEpic(5), 7); // 5
        taskManager.getSubtask(taskManager.getEpic(5), 8); // 6 и 7
        taskManager.getSubtask(taskManager.getEpic(6), 10); // 8
        //when
        int historyManagerSizeExpected = 8;
        List<Task> list = taskManager.getHistoryManager().getHistory();
        int historyManagerSizeActual = list.size(); // Должно быть 8
        //then
        assertNotEquals(historyManagerSizeExpected, historyManagerSizeActual, "Размер отличаются");
    }

    @DisplayName("Проверяет метод add")
    @Test
    public void inMemoryHistoryManager_add_should_EqualsLists_MethodAdd_Test() {
        //when
        historyManager.add(task1); //Должна быть удалена
        historyManager.add(task2); //Должна быть удалена
        historyManager.add(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task4); //Должна быть удалена
        historyManager.add(epic1); //Должна быть удалена
        historyManager.add(task4);
        historyManager.add(epic2);
        historyManager.add(epic1);

        expected.add(task3);
        expected.add(task1);
        expected.add(task2);
        expected.add(task4);
        expected.add(epic2);
        expected.add(epic1);

        List<Task> actual = historyManager.getHistory();

        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Проверяет метод getHistory()")
    @Test
    public void inMemoryHistoryManager_getHistory_should_EqualsLists_Test() {
        //when
        taskManager.getTask(1); //Должна быть удалена
        taskManager.getTask(2); //Должна быть удалена
        taskManager.getTask(4);
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getEpic(5); //Должна быть удалена
        taskManager.getEpic(6);
        taskManager.getEpic(5);
        taskManager.getSubtask(epic1, 7);
        taskManager.getSubtask(epic2, 12);
        taskManager.getSubtask(epic1, 8);
        taskManager.getSubtask(epic1, 7);

        List<Task> actual = taskManager.getHistoryManager().getHistory();

        expected.add(task4);
        expected.add(task2);
        expected.add(task1);
        expected.add(epic2);
        expected.add(epic1);
        expected.add(subtask6);
        expected.add(subtask2);
        expected.add(subtask1);

        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Проверяет метод remove()")
    @Test
    public void inMemoryHistoryManager_remove_shouldReturnEqualsLists_Test1() {
        //when
        taskManager.getTask(1); //Должна быть удалена
        taskManager.getTask(2); //Должна быть удалена
        taskManager.getTask(4); //Должна быть удалена
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getEpic(5); //Должна быть удалена
        taskManager.getEpic(6);
        taskManager.getEpic(5);
        taskManager.getSubtask(epic1, 7); // Должна быть удалена
        taskManager.getSubtask(epic2, 12);
        taskManager.getSubtask(epic1, 7);
        taskManager.getSubtask(epic1, 8); //Должна быть удалена
        taskManager.getHistoryManager().remove(4);
        taskManager.getHistoryManager().remove(8);

        List<Task> actual = taskManager.getHistoryManager().getHistory();

        expected.add(task2);
        expected.add(task1);
        expected.add(epic2);
        expected.add(epic1);
        expected.add(subtask6);
        expected.add(subtask1);

        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Проверяет метод remove()")
    @Test
    public void inMemoryHistoryManager_remove_shouldReturnEqualsLists_Test2() {
        //when
        taskManager.getSubtask(epic1, 7);
        taskManager.getSubtask(epic1, 8);
        taskManager.getSubtask(epic1, 9);
        taskManager.getSubtask(epic2, 10);
        taskManager.getSubtask(epic2, 11);
        taskManager.getSubtask(epic2, 12);
        taskManager.getTask(1);
        taskManager.removeEpic(5);
        taskManager.removeEpic(6);

        List<Task> actual = taskManager.getHistoryManager().getHistory();

        expected.add(task1);

        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Проверяет метод removeAll()")
    @Test
    public void inMemoryHistoryManager_removeAll_shouldReturnEqualsLists_Test() {
        //when
        taskManager.getSubtask(epic1, 7);
        taskManager.getSubtask(epic1, 8);
        taskManager.getSubtask(epic1, 9);
        taskManager.getSubtask(epic2, 10);
        taskManager.getSubtask(epic2, 11);
        taskManager.getSubtask(epic2, 12);
        taskManager.getTask(1);
        taskManager.getHistoryManager().removeAll();

        List<Task> actual = taskManager.getHistoryManager().getHistory();

        //then
        assertEquals(expected, actual);
    }
}
