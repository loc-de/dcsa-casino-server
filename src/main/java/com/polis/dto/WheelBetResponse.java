package com.polis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class WheelBetResponse {

    private String resultColor;
    private String resultVideo;
    private BigDecimal resultMoney;

}
