package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] split = requestMapping(exchange);
        String typeHandler = split[uriFirstElement];
        int id = getId(split);

        try {
            if (id > 0) { //для URL /tasks/{id}
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getTask(id)));
                        System.out.println("Получили задачу по id: " + id);
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        manager.updateTask(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
                        sendModify(exchange);
                    }
                    case "DELETE" -> {
                        manager.removeTask(id);
                        sendModify(exchange);
                    }
                    default -> unsupportedHttpMethod(exchange, typeHandler);
                }
            } else { //для URL /tasks
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getAllTask()));
                        System.out.println("Получили список задач.");
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        manager.createTask(task);
                        if (!manager.getAllTask().contains(task)) {
                            System.out.println("Задача не добавлена.");
                            sendHasOverlaps(exchange);
                        } else {
                            System.out.println("Задача добавлена.");
                            sendModify(exchange);
                        }
                    }
                    default -> unsupportedHttpMethod(exchange, typeHandler);
                }
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
