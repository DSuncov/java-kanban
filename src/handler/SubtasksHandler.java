package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] split = requestMapping(exchange);
        String typeHandler = split[uriFirstElement];
        int id = getId(split);
        int idEpic = getIdEpic(split);

        try {
            if (id > 0 && idEpic == 0) { //для URL /subtasks/{id}
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getSubtaskById(id)));
                        System.out.println("Получили подзадачу по id: " + id);
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        manager.updateSubtask(subtask.getEpicId(), subtask.getId(), subtask.getTitle(), subtask.getDescription(), subtask.getStatus());
                        sendModify(exchange);
                    }
                    case "DELETE" -> {
                        manager.removeSubtask(id);
                        sendModify(exchange);
                    }
                    default -> unsupportedHttpMethod(exchange, typeHandler);
                }
            }

            if (id > 0 && idEpic > 0) { // для URL /subtasks/{id}/epics/{id}, где 1-й id - подзадача, 2-й - эпик
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getSubtask(manager.getEpic(idEpic), id)));
                        System.out.println("Получили подзадачу по id: " + id + "и id его эпика: " + idEpic);
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        manager.updateSubtask(subtask.getEpicId(), subtask.getId(), subtask.getTitle(), subtask.getDescription(), subtask.getStatus());
                        sendModify(exchange);
                    }
                    case "DELETE" -> {
                        manager.removeSubtask(id);
                        sendModify(exchange);
                    }
                    default -> unsupportedHttpMethod(exchange, typeHandler);
                }
            }

            if (id == 0) { // для URL /subtasks
                switch (exchange.getRequestMethod()) {
                    case "GET" -> {
                        sendText(exchange, gson.toJson(manager.getAllSubtask()));
                        System.out.println("Получили список подзадач.");
                    }
                    case "POST" -> {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        manager.createSubtask(manager.getEpic(idEpic), subtask);
                        if (!manager.getAllSubtask().contains(subtask)) {
                            System.out.println("Подзадача не добавлена.");
                            sendHasOverlaps(exchange);
                        } else {
                            System.out.println("Подзадача добавлена.");
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
