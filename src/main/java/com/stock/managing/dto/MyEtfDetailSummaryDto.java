package com.stock.managing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MyEtfDetailSummaryDto {

    private long totalInvested;     // 총 편입금액
    private long totalEvaluated;    // 현재 평가액
    private long profitAmount;      // 수익금
    private double profitRate;      // 수익률 (%)
}
