package com.polis.handler.auth;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.RegisterRequest;
import com.polis.model.User;
import com.polis.security.CryptoService;
import com.polis.security.PasswordService;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
public class RegisterAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(HttpExchange exchange) throws IOException {
        try {
            RegisterRequest request = objectMapper.readValue(
                    CryptoService.decrypt(exchange.getRequestBody().readAllBytes()),
                    RegisterRequest.class
            );

            String username = request.getUsername();
            String password = request.getPassword();

            Optional<User> optionalUser = context.getUserRepository().findByUsername(username);

            if (optionalUser.isPresent()) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            User user = new User(null, username, PasswordService.hash(password), new BigDecimal(0));
            context.getUserRepository().insertUser(user);

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
