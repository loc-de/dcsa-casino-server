package com.polis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class WheelBetRequest {

    private int userId;
    private String betColor;
    private BigDecimal betAmount;

}
