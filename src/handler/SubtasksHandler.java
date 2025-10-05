package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Status;
import tasks.Subtask;

import java.io.IOException;

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
                        System.out.println("Введите данные для обновления.");
                        String newTitle = scanner.nextLine();
                        String newDescription = scanner.nextLine();
                        Status newStatus = Status.valueOf(scanner.nextLine());
                        manager.updateSubtask(idEpic, id, newTitle, newDescription, newStatus);
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
                        System.out.println("Введите данные для обновления.");
                        String newTitle = scanner.nextLine();
                        String newDescription = scanner.nextLine();
                        Status newStatus = Status.valueOf(scanner.nextLine());
                        manager.updateSubtask(idEpic, id, newTitle, newDescription, newStatus);
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
                        System.out.println("Введите данные для добавления.");
                        String title = scanner.nextLine();
                        String description = scanner.nextLine();
                        Status status = Status.valueOf(scanner.nextLine());
                        String start = scanner.nextLine();
                        Long duration = 60L;
                        Subtask subtask = new Subtask(title, description, status, start,duration);
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
