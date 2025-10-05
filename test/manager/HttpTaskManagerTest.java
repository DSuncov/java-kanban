package manager;

import api.HttpTaskServer;
import com.google.gson.Gson;
import exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTask();
        manager.removeAllEpic();
        manager.removeAllSubtask();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 2", Status.NEW, LocalDateTime.now().format(formatter), 5L);
        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2", Status.NEW);
        manager.createEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpic();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test 2", tasksFromManager.get(0).getTitle(), "Некорректное имя эпика");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2", Status.NEW);
        Subtask subtask = new Subtask("Test 3", "Testing subtask 3", Status.NEW, LocalDateTime.now().format(formatter), 10L);
        manager.createSubtask(epic, subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubtask();

        assertNotNull(tasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Test 3", tasksFromManager.get(0).getTitle(), "Некорректное имя подзадачи");
    }

    @Test
    public void testRemoveTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 2", Status.NEW, LocalDateTime.now().format(formatter), 5L);
        manager.createTask(task);
        manager.removeTask(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode());

        assertThrows(NotFoundException.class, () -> manager.getAllTask());
    }

    @Test
    public void testRemoveEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2", Status.NEW);
        manager.createEpic(epic);
        manager.removeEpic(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode());

        assertThrows(NotFoundException.class, () -> manager.getAllEpic());
    }

    @Test
    public void testRemoveSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2", Status.NEW);
        Subtask subtask = new Subtask("Test 3", "Testing subtask 3", Status.NEW, LocalDateTime.now().format(formatter), 10L);
        manager.createEpic(epic);
        manager.createSubtask(epic, subtask);
        manager.removeSubtask(epic, 2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        assertTrue(manager.getAllSubtask().isEmpty());
    }
}
