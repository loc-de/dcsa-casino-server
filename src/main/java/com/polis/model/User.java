package com.polis.model;

import java.math.BigDecimal;

public record User(
        Integer userId,
        String username,
        String hashPassword,
        BigDecimal balance
) {}
