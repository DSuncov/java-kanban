package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static manager.Fields.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

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

    @DisplayName("Проверяет список созданных задач в @BeforeAll на null и сравнивает две задачи на равенство")
    @Test
    void should_EqualsTasks_IfTheirIdEquals_Test() {
        //given
        Task expectedTask = new Task(TASK_TITLE[0], TASK_DESCRIPTION[0], DEFAULT_STATUS);
        expectedTask.setId(1);
        int taskId = expectedTask.getId();
        //when
        Task actualTask = taskManager.getTask(taskId);
        //then
        assertNotNull(actualTask, "Задача не найдена.");
        assertEquals(expectedTask, actualTask, "Задачи не совпадают.");
        //when
        final List<Task> tasks = taskManager.getAllTask();
        //then
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(4, tasks.size(), "Неверное количество задач.");
        assertEquals(expectedTask, tasks.get(0), "Задачи не совпадают.");
    }

    @DisplayName("Проверяет список созданных эпиков в @BeforeAll на null и сравнивает два эпика на равенство")
    @Test
    void should_EqualsEpics_IfTheirIdEquals_Test() {
        //given
        Task expectedEpic = new Epic(TASK_TITLE[4], TASK_DESCRIPTION[4], DEFAULT_STATUS);
        expectedEpic.setId(5);
        int epicId = expectedEpic.getId();
        //when
        Task actualEpic = taskManager.getEpic(epicId);
        //then
        assertNotNull(actualEpic, "Задача не найдена.");
        assertEquals(expectedEpic, actualEpic, "Задачи не совпадают.");
        //when
        final List<Epic> epics = taskManager.getAllEpic();
        //then
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(expectedEpic, epics.get(0), "Задачи не совпадают.");
    }

    @DisplayName("Проверяет список созданных подзадач в @BeforeAll на null и сравнивает две подзадачи на равенство")
    @Test
    void should_EqualsSubtasks_IfTheirIdEquals_Test() {
        //given
        Task expectedSubtask = new Subtask(SUBTASK_TITLE[3], SUBTASK_DESCRIPTION[3], DEFAULT_STATUS);
        expectedSubtask.setId(1);
        int subtaskId = expectedSubtask.getId();
        //when
        Task actualSubtask = taskManager.getSubtask(taskManager.getEpic(6), subtaskId);
        //then
        assertNotNull(actualSubtask, "Задача не найдена.");
        assertEquals(expectedSubtask, actualSubtask, "Задачи не совпадают.");
        //when
        final List<Subtask> subtasks = taskManager.getSubtaskByEpic(taskManager.getEpic(6));
        //then
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(expectedSubtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @DisplayName("Проверяет, что метод getDefault() возвращает проинициализированный объект TaskManager")
    @Test
    void Managers_GetDefault_shouldReturnInitializedTaskManager_Test() {
        //given-when
        TaskManager taskManager = Managers.getDefault();
        //then
        assertNotNull(taskManager, "Объект не пронициализирован.");
        //Проверяем готовность к работе
        assertEquals(0, taskManager.getTasksList().size());
        assertTrue(taskManager.getEpicsList().isEmpty());
    }

    @DisplayName("Проверяет, что метод getDefaultHistory() возвращает проинициализированный объект HistoryManager")
    @Test
    void Managers_GetDefaultHistory_shouldReturnInitializedHistoryManager_Test() {
        //given-when
        HistoryManager taskManager = Managers.getDefaultHistory();
        //then
        assertNotNull(taskManager, "Объект не пронициализирован.");
        //Проверяем готовность к работе
        assertEquals(0, taskManager.getHistory().size());
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @DisplayName("Проверяет, что метод возвращает по id ожидаемые задачи")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getTaskById")
    void InMemoryTaskManager_getTask_shouldReturnExpectedTaskById_Test(Task returnExpected, int id) {
        //given-when
        returnExpected.setId(id);
        //then
        assertEquals(returnExpected, taskManager.getTask(id));
    }

    @DisplayName("Проверяет, что метод возвращает по id ожидаемые эпики")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getEpicById")
    void InMemoryTaskManager_getEpic_shouldReturnExpectedEpicById_Test(Epic returnExpected, int id) {
        //given-when
        returnExpected.setId(id);
        //then
        assertEquals(returnExpected, taskManager.getEpic(id));
    }

    @DisplayName("Проверяет, что метод возвращает по id ожидаемые подзадачи")
    @ParameterizedTest
    @MethodSource("manager.Stubs#getSubtaskById")
    void InMemoryTaskManager_getSubtask_shouldReturnExpectedSubtaskByEpicAndId_Test(Subtask returnExpected, Epic epic, int id) {
        //given-when
        returnExpected.setId(id);
        //then
        assertEquals(returnExpected, taskManager.getSubtask(epic, id));
    }

    @DisplayName("Проверяет, что метод добавляет в список задачу с установленным id, а не меняет его через инкрементацию в idCounter")
    @Test
    void InMemoryTaskManager_getTask_shouldReturnTask_WithSetId_Test() {
        //given
        Task task = new Task("Задача № 10", "Описание № 10", 10, DEFAULT_STATUS);
        //when
        taskManager.createTask(task);
        //then
        assertEquals(task, taskManager.getTask(10));

        taskManager.removeTask(10);
    }

    @DisplayName("Проверяет, что созданный объект задачи не изменяет свои поля после добавления в список задач")
    @Test
    void InMemoryTaskManager_getTask_shouldReturnImmutableTask_AfterAddToList_Test() {
        //given
        Task task = new Task("Задача № 7", "Описание № 7", DEFAULT_STATUS);
        String expectedTitle = task.getTitle();
        String expectedDescription = task.getDescription();
        int expectedId = 7;
        Status expectedStatus = task.getStatus();
        //when
        taskManager.createTask(task);
        //then
        assertEquals(expectedTitle, taskManager.getTask(7).getTitle());
        assertEquals(expectedDescription, taskManager.getTask(7).getDescription());
        assertEquals(expectedId, taskManager.getTask(7).getId());
        assertEquals(expectedStatus, taskManager.getTask(7).getStatus());
    }
}