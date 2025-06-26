package com.polis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class InfoResponse {

    private int userId;
    private String username;
    private BigDecimal balance;

}
