package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketIndicatorDTO {

    private String date;         // yyyy-MM-dd
    private String code;         // 종목 코드
    private Double close;
    private Double changeAmount;
    private Double changeRate;
    private String lastUpdate;   // DATETIME → 문자열 그대로
}
