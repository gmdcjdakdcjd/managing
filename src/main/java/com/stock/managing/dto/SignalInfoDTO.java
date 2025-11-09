package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalInfoDTO {
    private String name;           // 종목명
    private String code;           // 종목코드
    private String strategyName;   // 전략명
    private String signalDate;     // 포착일
    private Long resultId;         // 전략 결과 ID
}