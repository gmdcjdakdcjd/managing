package com.stock.managing.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyEtfSummaryDto {

    private String etfName;
    private String etfDescription;

    private Long itemCount;
    private Double investedAmount;   // 총 편입금액
    private Double evaluatedAmount;  // 현재 평가금액
    private Double profitRate;       // 수익률 (%)
}
