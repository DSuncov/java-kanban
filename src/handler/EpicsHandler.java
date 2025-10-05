package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        String typeHandler = split[uriFirstElement];
        int id = getId(split);

        try {
            if (id > 0) { // для URL /epics/{id}
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        if (split.length == 4 && "subtasks".equals(split[uriThirdElement])) { // для URL /epics/{id}/subtasks
                            sendText(exchange, gson.toJson(manager.getSubtaskByEpic(manager.getEpic(id))));
                            System.out.println("Получили список подзадач по id эпика: " + id);
                        } else {
                            sendText(exchange, gson.toJson(manager.getEpic(id)));
                            System.out.println("Получили эпик по id: " + id);
                        }
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Epic epic = gson.fromJson(body, Epic.class);
                        manager.updateEpic(epic.getId(), epic.getTitle(), epic.getDescription());
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
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Epic epic = gson.fromJson(body, Epic.class);
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
