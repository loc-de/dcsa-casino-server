package com.polis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SlotsBetResponse {

    private String[] resultCombination;
    private BigDecimal resultMoney;

}
