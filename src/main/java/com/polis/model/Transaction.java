package com.polis.model;

import java.math.BigDecimal;

public record Transaction(
        Integer transactionId,
        Integer userId,
        BigDecimal moneyAmount
) {}

