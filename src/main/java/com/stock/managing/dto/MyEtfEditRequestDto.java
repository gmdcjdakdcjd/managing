package com.stock.managing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyEtfEditRequestDto {

    /** ETF 이름 (식별자) */
    private String etfName;

    /** 편집 대상 종목 목록 */
    private List<MyEtfEditItemDto> items;
}
