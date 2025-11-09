package com.stock.managing.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 종목 기본정보 + 시세 데이터 DTO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@ToString
public class StockDTO {
    private String code;
    private String name;
    private String marketType;
    private Map<String, Object> companyInfo;
    private List<PriceDTO> priceList;
}
