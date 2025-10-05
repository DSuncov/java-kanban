package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public HistoryHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String typeHandler = requestMapping(exchange)[URI_FIRST_ELEMENT];
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> {
                    sendText(exchange, gson.toJson(manager.getHistoryManager().getHistory()));
                    System.out.println("Получили историю задач.");
                }
                default -> unsupportedHttpMethod(exchange, typeHandler);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
