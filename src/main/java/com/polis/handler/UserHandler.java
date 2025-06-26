package com.polis.handler;

import com.polis.ApplicationContext;
import com.polis.handler.user.CodeAction;
import com.polis.handler.user.InfoAction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class UserHandler implements HttpHandler {

    private final ApplicationContext context;
    private final CodeAction codeAction;
    private final InfoAction infoAction;

    public UserHandler(ApplicationContext context) {
        this.context = context;
        this.codeAction = new CodeAction(context);
        this.infoAction = new InfoAction(context);
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

        if (path.endsWith("/info")) {
            infoAction.handle(exchange);
        } else if (path.endsWith("/code")) {
            codeAction.handle(exchange);
        } else {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
    }
}
