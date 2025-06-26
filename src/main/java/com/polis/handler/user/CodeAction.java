package com.polis.handler.user;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.CodeRequest;
import com.polis.model.Transaction;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;

@AllArgsConstructor
public class CodeAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(HttpExchange exchange) throws IOException {
        try {
            CodeRequest request = objectMapper.readValue(exchange.getRequestBody(), CodeRequest.class);

            int userId = request.getUserId();
            String code = request.getCode();

            if (!code.matches("^CODE\\d+$")) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            String numberString = code.substring(4);
            BigDecimal parsedNumber;
            try {
                parsedNumber = new BigDecimal(Integer.parseInt(numberString));
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            Transaction transaction = new Transaction(null, userId, parsedNumber);

            context.getUserRepository().updateBalance(userId, parsedNumber);
            context.getTransactionRepository().insertTransaction(transaction);

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        } catch (DatabindException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }

    }

}
