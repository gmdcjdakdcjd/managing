package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyResultDTO {

    private String strategyName;
    private String signalDate;
    private String signalType;
    private Integer totalData;
    private String createdAt;
}
