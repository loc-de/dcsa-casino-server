package com.polis.handler.game;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.SlotsBetRequest;
import com.polis.dto.SlotsBetResponse;
import com.polis.model.Bet;
import com.polis.security.CryptoService;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@AllArgsConstructor
public class SlotsAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Random random = new Random();
    private static final String[] VARIATIONS = {"LEMON", "SEVEN", "BAR", "CHERRY"};

    public void handle(HttpExchange exchange) throws IOException {
        try {
            SlotsBetRequest request = objectMapper.readValue(
                    CryptoService.decrypt(exchange.getRequestBody().readAllBytes()),
                    SlotsBetRequest.class
            );

            int userId = request.getUserId();
            BigDecimal betAmount = request.getBetAmount();

            if (betAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                    !context.getUserService().hasSufficientBalance(userId, betAmount)
            ) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            String[] combination = new String[3];

            // Game logics

            for (int i = 0; i < 3; i++) {
                combination[i] = VARIATIONS[random.nextInt(VARIATIONS.length)];
            }

            BigDecimal resultMoney = calculateResultMoney(combination, betAmount);

            BigDecimal balanceDiff;
            if (resultMoney.compareTo(BigDecimal.ZERO) == 0) {
                balanceDiff = betAmount.negate();
            } else {
                balanceDiff = resultMoney.subtract(betAmount);
            }
            Bet bet = new Bet(null, userId, 1, balanceDiff);
            context.getBetRepository().insertBet(bet);;

            SlotsBetResponse response = new SlotsBetResponse(combination, resultMoney);

            String json = objectMapper.writeValueAsString(response);
            byte[] bytes = CryptoService.encrypt(json.getBytes(StandardCharsets.UTF_8));

            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
        catch (DatabindException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }

    public BigDecimal calculateResultMoney(String[] combination, BigDecimal bet) {
        boolean isWinning = combination[0].equals(combination[1])
                && combination[1].equals(combination[2]);

        if (isWinning)
        {
            // here you can adjust coefficients
            return switch (combination[0]) {
                case "LEMON", "CHERRY" -> bet.multiply(BigDecimal.valueOf(4));
                case "BAR" -> bet.multiply(BigDecimal.valueOf(1));
                default ->  //seven
                        bet.multiply(BigDecimal.valueOf(10));
            };
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }
}
