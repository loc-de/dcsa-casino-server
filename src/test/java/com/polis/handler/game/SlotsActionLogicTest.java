package com.polis.handler.game;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@Tag("game")
public class SlotsActionLogicTest
{

    private final SlotsAction logic = new SlotsAction(null);

    @Test
    void testWinLemon() {
        String[] combo = {"LEMON", "LEMON", "LEMON"};
        BigDecimal bet = BigDecimal.valueOf(10);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(4));

        assertEquals(expected, logic.calculateResultMoney(combo, bet));
    }

    @Test
    void testWinWCherry() {
        String[] combo = {"CHERRY", "CHERRY", "CHERRY"};
        BigDecimal bet = BigDecimal.valueOf(5);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(4));

        assertEquals(expected, logic.calculateResultMoney(combo, bet));
    }

    @Test
    void testWinBar() {
        String[] combo = {"BAR", "BAR", "BAR"};
        BigDecimal bet = BigDecimal.valueOf(12);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(1));

        assertEquals(expected, logic.calculateResultMoney(combo, bet));
    }

    @Test
    void testWinSeven() {
        String[] combo = {"SEVEN", "SEVEN", "SEVEN"};
        BigDecimal bet = BigDecimal.valueOf(3);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(10));

        assertEquals(expected, logic.calculateResultMoney(combo, bet));
    }

    @Test
    void testLose() {
        String[] combo = {"LEMON", "BAR", "CHERRY"};
        BigDecimal bet = BigDecimal.valueOf(20);

        assertEquals(BigDecimal.ZERO, logic.calculateResultMoney(combo, bet));
    }

    @Test
    void testTwo() {
        String[] combo = {"LEMON", "LEMON", "BAR"};
        BigDecimal bet = BigDecimal.valueOf(100);

        assertEquals(BigDecimal.ZERO, logic.calculateResultMoney(combo, bet));
    }
}
