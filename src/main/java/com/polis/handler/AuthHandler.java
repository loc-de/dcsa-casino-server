package com.polis.handler;

import com.polis.ApplicationContext;
import com.polis.handler.auth.LoginAction;
import com.polis.handler.auth.RegisterAction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthHandler implements HttpHandler {

    private final LoginAction loginAction;
    private final RegisterAction registerAction;

    public AuthHandler(ApplicationContext context) {
        this.loginAction = new LoginAction(context);
        this.registerAction = new RegisterAction(context);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath();

        if (path.endsWith("/login")) {
            loginAction.handle(exchange);
        } else if (path.endsWith("/register")) {
            registerAction.handle(exchange);
        } else {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
    }

}
