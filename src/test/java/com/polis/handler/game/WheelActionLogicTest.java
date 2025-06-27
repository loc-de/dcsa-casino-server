package com.polis.handler.game;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("game")
public class WheelActionLogicTest
{

    private final WheelAction logic = new WheelAction(null);

    @Test
    void testCalculateRedWin()
    {
        BigDecimal bet = BigDecimal.valueOf(50);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(2));

        BigDecimal result = logic.calculateResultMoney("red", bet);
        assertEquals(expected, result);
    }

    @Test
    void testCalculateWhiteWin()
    {
        BigDecimal bet = BigDecimal.valueOf(30);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(2));

        BigDecimal result = logic.calculateResultMoney("white", bet);
        assertEquals(expected, result);
    }

    @Test
    void testCalculateGreenWin()
    {
        BigDecimal bet = BigDecimal.valueOf(10);
        BigDecimal expected = bet.multiply(BigDecimal.valueOf(10));

        BigDecimal result = logic.calculateResultMoney("green", bet);
        assertEquals(expected, result);
    }

    @Test
    void testRandomVideoRedIsValid()
    {
        for (int i = 0; i < 20; i++)
        {
            String video = logic.randomVideo("red");
            assertTrue(List.of("red 1", "red 2").contains(video));
        }
    }

    @Test
    void testRandomVideoWhiteIsValid()
    {
        for (int i = 0; i < 20; i++)
        {
            String video = logic.randomVideo("white");
            assertTrue(List.of("white 1", "white 2").contains(video));
        }
    }

    @Test
    void testRandomVideoGreenIsValid()
    {
        for (int i = 0; i < 20; i++)
        {
            String video = logic.randomVideo("green");
            assertEquals("green", video);
        }
    }

    @Test
    void testRandomVideoFallbackDefault()
    {
        String video = logic.randomVideo("no_color");
        assertEquals("default", video);
    }
}
