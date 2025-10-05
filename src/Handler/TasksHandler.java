package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Status;
import tasks.Task;

import java.io.IOException;

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
        String typeHandler = split[URI_FIRST_ELEMENT];
        int id = getId(split);

        try {
            if (id > 0) { //для URL /tasks/{id}
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getTask(id)));
                        System.out.println("Получили задачу по id: " + id);
                    }
                    case "POST" -> {
                        System.out.println("Введите данные для обновления.");
                        String newTitle = scanner.nextLine();
                        String newDescription = scanner.nextLine();
                        Status newStatus = Status.valueOf(scanner.nextLine());
                        manager.updateTask(id, newTitle, newDescription, newStatus);
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
                        System.out.println("Введите данные для добавления.");
                        String title = scanner.nextLine();
                        String description = scanner.nextLine();
                        Status status = Status.valueOf(scanner.nextLine());
                        String start = scanner.nextLine();
                        Long duration = 60L;
                        Task task = new Task(title, description, status, start, duration);
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
