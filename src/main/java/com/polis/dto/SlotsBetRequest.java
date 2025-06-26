package com.polis.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SlotsBetRequest {

    private int userId;
    private BigDecimal betAmount;

}
