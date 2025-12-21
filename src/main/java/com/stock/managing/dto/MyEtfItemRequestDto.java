package com.stock.managing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyEtfItemRequestDto {

    private String etfName;           // ETF 이름
    private String etfDescription;    // ETF 설명

    private String code;
    private String name;
    private Integer quantity;
    private String memo;
}
