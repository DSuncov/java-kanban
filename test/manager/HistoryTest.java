package manager;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static manager.Fields.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryTest {

    static HistoryManager historyManager = Managers.getDefaultHistory();
    static TaskManager taskManagerManager = Managers.getDefault();

    List<Task> expected = new ArrayList<>();
    static Task task1 = new Task(TASK_TITLE[0], TASK_DESCRIPTION[0], DEFAULT_STATUS);
    static Task task2 = new Task(TASK_TITLE[1], TASK_DESCRIPTION[1], DEFAULT_STATUS);
    static Task task3 = new Task(TASK_TITLE[2], TASK_DESCRIPTION[2], DEFAULT_STATUS);
    static Task task4 = new Task(TASK_TITLE[3], TASK_DESCRIPTION[3], DEFAULT_STATUS);
    static Epic epic1 = new Epic(TASK_TITLE[4], TASK_DESCRIPTION[4], DEFAULT_STATUS);
    static Epic epic2 = new Epic(TASK_TITLE[5], TASK_DESCRIPTION[5], DEFAULT_STATUS);
    static Subtask subtask1 = new Subtask(SUBTASK_TITLE[0], SUBTASK_DESCRIPTION[0], DEFAULT_STATUS);
    static Subtask subtask2 = new Subtask(SUBTASK_TITLE[1], SUBTASK_DESCRIPTION[1], DEFAULT_STATUS);
    static Subtask subtask3 = new Subtask(SUBTASK_TITLE[2], SUBTASK_DESCRIPTION[2], DEFAULT_STATUS);
    static Subtask subtask4 = new Subtask(SUBTASK_TITLE[3], SUBTASK_DESCRIPTION[3], DEFAULT_STATUS);
    static Subtask subtask5 = new Subtask(SUBTASK_TITLE[4], SUBTASK_DESCRIPTION[4], DEFAULT_STATUS);
    static Subtask subtask6 = new Subtask(SUBTASK_TITLE[5], SUBTASK_DESCRIPTION[5], DEFAULT_STATUS);

    @BeforeAll
    public static void createTask() {
        taskManagerManager.createTask(task1);
        taskManagerManager.createTask(task2);
        taskManagerManager.createTask(task3);
        taskManagerManager.createTask(task4);
        taskManagerManager.createEpic(epic1);
        taskManagerManager.createEpic(epic2);
        taskManagerManager.createSubtask(epic1, subtask1);
        taskManagerManager.createSubtask(epic1, subtask2);
        taskManagerManager.createSubtask(epic1, subtask3);
        taskManagerManager.createSubtask(epic2, subtask4);
        taskManagerManager.createSubtask(epic2, subtask5);
        taskManagerManager.createSubtask(epic2, subtask6);
    }

    @AfterEach
    public void clearList() {
        expected.clear();
        taskManagerManager.getHistoryManager().removeAll();
    }

    @DisplayName("Проверяет метод add")
    @Test
    public void should_EqualsLists_MethodAdd_Test() {
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

    @DisplayName("Проверяет метод getTask/GetEpic/GetSubtask")
    @Test
    public void should_EqualsLists_MethodGet_Test() {
        //when
        taskManagerManager.getTask(1); //Должна быть удалена
        taskManagerManager.getTask(2); //Должна быть удалена
        taskManagerManager.getTask(4);
        taskManagerManager.getTask(2);
        taskManagerManager.getTask(1);
        taskManagerManager.getEpic(5); //Должна быть удалена
        taskManagerManager.getEpic(6);
        taskManagerManager.getEpic(5);
        taskManagerManager.getSubtask(epic1, 7);
        taskManagerManager.getSubtask(epic2, 12);
        taskManagerManager.getSubtask(epic1, 8);
        taskManagerManager.getSubtask(epic1, 7);

        List<Task> actual = taskManagerManager.getHistoryManager().getHistory();

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
    public void should_EqualsLists_MethodRemove1_Test() {
        //when
        taskManagerManager.getTask(1); //Должна быть удалена
        taskManagerManager.getTask(2); //Должна быть удалена
        taskManagerManager.getTask(4); //Должна быть удалена
        taskManagerManager.getTask(2);
        taskManagerManager.getTask(1);
        taskManagerManager.getEpic(5); //Должна быть удалена
        taskManagerManager.getEpic(6);
        taskManagerManager.getEpic(5);
        taskManagerManager.getSubtask(epic1, 7); // Должна быть удалена
        taskManagerManager.getSubtask(epic2, 12);
        taskManagerManager.getSubtask(epic1, 7);
        taskManagerManager.getSubtask(epic1, 8); //Должна быть удалена
        taskManagerManager.getHistoryManager().remove(4);
        taskManagerManager.getHistoryManager().remove(8);

        List<Task> actual = taskManagerManager.getHistoryManager().getHistory();

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
    public void should_EqualsLists_MethodRemove2_Test() {
        //when
        taskManagerManager.getSubtask(epic1, 7);
        taskManagerManager.getSubtask(epic1, 8);
        taskManagerManager.getSubtask(epic1, 9);
        taskManagerManager.getSubtask(epic2, 10);
        taskManagerManager.getSubtask(epic2, 11);
        taskManagerManager.getSubtask(epic2, 12);
        taskManagerManager.getTask(1);
        taskManagerManager.removeEpic(5);
        taskManagerManager.removeEpic(6);

        List<Task> actual = taskManagerManager.getHistoryManager().getHistory();

        expected.add(task1);

        //then
        assertEquals(expected, actual);
    }
}
