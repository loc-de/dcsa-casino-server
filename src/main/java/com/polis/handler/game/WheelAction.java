package com.polis.handler.game;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polis.ApplicationContext;
import com.polis.dto.WheelBetRequest;
import com.polis.dto.WheelBetResponse;
import com.polis.model.Bet;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class WheelAction {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Random random = new Random();

    private static final List<String> RED_VIDEOS = List.of("red 1", "red 2");
    private static final List<String> GREEN_VIDEOS = List.of("green");
    private static final List<String> WHITE_VIDEOS = List.of("white 1", "white 2");

    public void handle(HttpExchange exchange) throws IOException {
        try {
            WheelBetRequest request = objectMapper.readValue(exchange.getRequestBody(), WheelBetRequest.class);

            int userId = request.getUserId();
            String betColor = request.getBetColor().toLowerCase();
            BigDecimal betAmount = request.getBetAmount();

            if (betAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                    !context.getUserService().hasSufficientBalance(userId, betAmount)
            ) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            // Color check
            List<String> validColors = List.of("red", "white", "green");
            if (!validColors.contains(betColor)) {
                exchange.sendResponseHeaders(400, 0);
                exchange.close();
                return;
            }

            // Game logics

            String resultColor = randomColor();
            String resultVideo = randomVideo(resultColor);

            BigDecimal resultMoney = betColor.equals(resultColor)
                    ? calculateResultMoney(resultColor, betAmount)
                    : BigDecimal.ZERO;

            BigDecimal balanceDiff;
            if (resultMoney.compareTo(BigDecimal.ZERO) == 0) {
                balanceDiff = betAmount.negate();
            } else {
                balanceDiff = resultMoney.subtract(betAmount);
            }
            Bet bet = new Bet(null, userId, 2, balanceDiff);
            context.getBetRepository().insertBet(bet);

            WheelBetResponse response = new WheelBetResponse(resultColor, resultVideo, resultMoney);

            String json = objectMapper.writeValueAsString(response);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
        catch (DatabindException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }

    public String randomColor() {
        int roll = random.nextInt(100);
        if (roll < 48) return "red";
        else if (roll < 96) return "white";
        else return "green";
    }

    public BigDecimal calculateResultMoney(String color, BigDecimal bet) {
        if (color.equals("red")) {
            return bet.multiply(BigDecimal.valueOf(2));
        } else if (color.equals("white")) {
            return bet.multiply(BigDecimal.valueOf(2));
        } else {
            return bet.multiply(BigDecimal.valueOf(10));
        }
    }

    public String randomVideo(String resultColor){
        return switch (resultColor) {
            case "red" -> RED_VIDEOS.get(random.nextInt(RED_VIDEOS.size()));
            case "white" -> WHITE_VIDEOS.get(random.nextInt(WHITE_VIDEOS.size()));
            case "green" -> GREEN_VIDEOS.get(random.nextInt(GREEN_VIDEOS.size()));
            default -> "default";
        };
    }
}
