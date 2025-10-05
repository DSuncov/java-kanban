package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BaseHandler {

    protected final int uriFirstElement = 1; //Базовый путь
    protected final int uriSecondElement = 2; //id задачи, эпика или подзадачи
    protected final int uriThirdElement = 3; //Тип второй задачи
    protected final int uriFourthElement = 4; //id эпика для методов подзадач: /subtasks/{id}/epics/{id}

    private final TaskManager manager;
    private final Gson gson;
    protected final Scanner scanner = new Scanner(System.in);

    public BaseHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    public void sendText(HttpExchange exchange, String responseString) throws IOException {
        byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(resp);
        }
        exchange.close();
    }

    public void unsupportedHttpMethod(HttpExchange exchange, String typeHandler) throws IOException {
        System.out.printf("Для %s метод %s не поддерживается + \n", exchange.getRequestMethod(), typeHandler);
        exchange.sendResponseHeaders(405, 0);
        exchange.close();
    }

    public void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    public void sendHasOverlaps(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.close();
    }

    public void sendModify(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    public String[] requestMapping(HttpExchange exchange) {
        return exchange.getRequestURI().getPath().split("/");
    }

    public int getId(String[] split) {
        int id = 0;

        if (split.length == 3) {
            try {
                id = Integer.parseInt(split[uriSecondElement]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("В качестве id передано не целое число.");
            }
        }

        if (split.length == 5) {
            try {
                id = Integer.parseInt(split[uriSecondElement]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("В качестве id передано не целое число.");
            }
        }
        return id;
    }

    public int getIdEpic(String[] split) {
        int idEpic = 0;

        if (split.length == 5) {
            try {
                idEpic = Integer.parseInt(split[uriFourthElement]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("В качестве id передано не целое число.");
            }
        }
        return idEpic;
    }
}
