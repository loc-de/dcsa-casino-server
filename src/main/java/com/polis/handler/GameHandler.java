package com.polis.handler;

import com.polis.ApplicationContext;
import com.polis.handler.game.SlotsAction;
import com.polis.handler.game.WheelAction;
import com.polis.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;

public class GameHandler implements HttpHandler {

    private final ApplicationContext context;
    private final SlotsAction slotsAction;
    private final WheelAction wheelAction;

    public GameHandler(ApplicationContext context) {
        this.context = context;
        this.slotsAction = new SlotsAction(context);
        this.wheelAction = new WheelAction(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
            return;
        }

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();
            return;
        }

        String token = authHeader.substring(7);
        if (!context.getJwtService().isValidToken(token)) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath();

        if (path.endsWith("/slots")) {
            slotsAction.handle(exchange);
        } else if (path.endsWith("/wheel")) {
            wheelAction.handle(exchange);
        } else {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
    }

}
