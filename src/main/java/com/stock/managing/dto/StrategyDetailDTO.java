package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyDetailDTO {

    private String resultId;
    private String signalDate;
    private String code;
    private String name;
    private String action;
    private Long  price;
    private Long  prevClose;
    private Long  diff;
    private Long  volume;
    private Integer specialValue;
    private String createdAt;
}
