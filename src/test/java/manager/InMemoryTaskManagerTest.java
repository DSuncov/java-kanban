package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

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

    @Test
    void shouldEqualsTaskIfIdEquals() {
        Task expectedTask = new Task("Задача № 1", "Описание № 1", Status.NEW);
        expectedTask.setId(1);
        int taskId = expectedTask.getId();
        Task actualTask = taskManager.getTask(taskId);

        assertNotNull(actualTask, "Задача не найдена.");
        assertEquals(expectedTask, actualTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(4, tasks.size(), "Неверное количество задач.");
        assertEquals(expectedTask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldEqualsEpicIfIdEquals() {
        Task expectedEpic = new Epic("Задача № 5", "Описание № 5", Status.NEW);
        expectedEpic.setId(5);
        int epicId = expectedEpic.getId();
        Task actualEpic = taskManager.getEpic(epicId);

        assertNotNull(actualEpic, "Задача не найдена.");
        assertEquals(expectedEpic, actualEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(expectedEpic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldEqualsSubtaskIfIdEquals() {
        Task expectedSubtask = new Subtask("Подзадача № 2.1", "Описание № 2.1", Status.NEW);
        expectedSubtask.setId(1);
        int subtaskId = expectedSubtask.getId();
        Task actualSubtask = taskManager.getSubtask(taskManager.getEpic(6), subtaskId);

        assertNotNull(actualSubtask, "Задача не найдена.");
        assertEquals(expectedSubtask, actualSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtaskByEpic(taskManager.getEpic(6));

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(expectedSubtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Объект не пронициализирован.");
        //Проверяем готовность к работе
        assertEquals(0, taskManager.getTasksList().size());
        assertTrue(taskManager.getEpicsList().isEmpty());
    }

    @Test
    void shouldReturnHistoryManager() {
        HistoryManager taskManager = Managers.getDefaultHistory();
        assertNotNull(taskManager, "Объект не пронициализирован.");
        //Проверяем готовность к работе
        assertEquals(0, taskManager.getHistory().size());
        assertTrue(taskManager.getHistory().isEmpty());
    }

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

    @ParameterizedTest
    @MethodSource("getTaskById")
    void shouldReturnTaskById(Task returnExpected, int id) {
        returnExpected.setId(id);
        assertEquals(returnExpected, taskManager.getTask(id));
    }

    @ParameterizedTest
    @MethodSource("getEpicById")
    void shouldReturnEpicById(Epic returnExpected, int id) {
        returnExpected.setId(id);
        assertEquals(returnExpected, taskManager.getEpic(id));
    }

    @ParameterizedTest
    @MethodSource("getSubtaskById")
    void shouldReturnSubtaskById(Subtask returnExpected, Epic epic, int id) {
        returnExpected.setId(id);
        assertEquals(returnExpected, taskManager.getSubtask(epic, id));
    }

    @Test
    void shouldNotConflictBetweenTaskWithSetAndGenerateId() {
        Task task = new Task("Задача № 10", "Описание № 10", 10, Status.NEW);
        taskManager.createTask(task);

        assertEquals(task, taskManager.getTask(10));

        taskManager.removeTask(10);
    }

    @Test
    void shouldImmutableTask() {
        Task task = new Task("Задача № 7", "Описание № 7", Status.NEW);

        String expectedTitle = task.getTitle();
        String expectedDescription = task.getDescription();
        int expectedId = 7;
        Status expectedStatus = task.getStatus();

        taskManager.createTask(task);

        assertEquals(expectedTitle, taskManager.getTask(7).getTitle());
        assertEquals(expectedDescription, taskManager.getTask(7).getDescription());
        assertEquals(expectedId, taskManager.getTask(7).getId());
        assertEquals(expectedStatus, taskManager.getTask(7).getStatus());
    }
}