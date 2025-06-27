package com.polis.handler.user;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.InfoResponse;
import com.polis.model.User;
import com.polis.security.CryptoService;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@AllArgsConstructor
public class InfoAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(HttpExchange exchange) throws IOException {
        try {
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            String token = authHeader.substring(7);
            String username = context.getJwtService().parse(token);

            Optional<User> optionalUser = context.getUserRepository().findByUsername(username);
            User user = optionalUser.get();
            InfoResponse response = new InfoResponse(user.userId(), user.username(), user.balance());

            String json = objectMapper.writeValueAsString(response);
            byte[] bytes = CryptoService.encrypt(json.getBytes(StandardCharsets.UTF_8));

            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        } catch (DatabindException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }

}
