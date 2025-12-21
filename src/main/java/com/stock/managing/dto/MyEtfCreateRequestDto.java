package com.stock.managing.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// ETF 생성 요청 전체
@Getter
@Setter
public class MyEtfCreateRequestDto {

    private String etfName;
    private String etfDescription;
    private List<MyEtfItemRequestDto> items;
}
