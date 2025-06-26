package com.polis.model;

import java.math.BigDecimal;

public record Game(
        Integer gameId,
        String name,
        String description,
        BigDecimal income
) {}
