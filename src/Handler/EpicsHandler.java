package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;

import java.io.IOException;

public class EpicsHandler extends BaseHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
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
            if (id > 0) { // для URL /epics/{id}
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        if ("subtasks".equals(split[URI_SECOND_ELEMENT])) { // для URL /epics/{id}/subtasks
                            sendText(exchange, gson.toJson(manager.getSubtaskByEpic(manager.getEpic(id))));
                            System.out.println("Получили список подзадач по id эпика: " + id);
                        } else {
                            sendText(exchange, gson.toJson(manager.getEpic(id)));
                            System.out.println("Получили эпик по id: " + id);
                        }
                    }
                    case "POST" -> {
                        System.out.println("Введите данные для обновления.");
                        String newTitle = scanner.nextLine();
                        String newDescription = scanner.nextLine();
                        manager.updateEpic(id, newTitle, newDescription);
                        sendModify(exchange);
                    }
                    case "DELETE" -> {
                        manager.removeEpic(id);
                        sendModify(exchange);
                    }
                    default -> unsupportedHttpMethod(exchange, typeHandler);
                }
            } else { // для URL /epics
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getAllEpic()));
                        System.out.println("Получили список эпиков.");
                    }
                    case "POST" -> {
                        System.out.println("Введите данные для добавления.");
                        String title = scanner.nextLine();
                        String description = scanner.nextLine();
                        Status status = Status.valueOf(scanner.nextLine());
                        Epic epic = new Epic(title, description, status);
                        manager.createEpic(epic);
                        if (!manager.getAllEpic().contains(epic)) {
                            System.out.println("Эпик не добавлен.");
                            sendHasOverlaps(exchange);
                        } else {
                            System.out.println("Эпик добавлен.");
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
