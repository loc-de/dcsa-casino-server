package com.polis.model;

import java.math.BigDecimal;

public record Bet(
        Integer betId,
        Integer userId,
        Integer gameId,
        BigDecimal balanceDiff
) {}
