package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketIndicatorDTO {

    private String date;
    private Double close;
    private Double changeAmount;
    private Double changeRate;
}