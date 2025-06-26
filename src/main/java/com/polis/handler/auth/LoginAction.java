package com.polis.handler.auth;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.LoginRequest;
import com.polis.model.User;
import com.polis.security.PasswordService;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class LoginAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(HttpExchange exchange) throws IOException {
        try {
            LoginRequest request = objectMapper.readValue(exchange.getRequestBody(), LoginRequest.class);

            String username = request.getUsername();
            String password = request.getPassword();

            Optional<User> optionalUser = context.getUserRepository().findByUsername(username);

            if (optionalUser.isEmpty() || !PasswordService.verify(password, optionalUser.get().hashPassword())) {
                exchange.sendResponseHeaders(401, 0);
                exchange.close();
                return;
            }

            String token = context.getJwtService().generate(username);

            exchange.getResponseHeaders().add("x-token", token);
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        } catch (DatabindException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }

    }

}
