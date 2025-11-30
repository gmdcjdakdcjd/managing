package com.stock.managing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondDailyYieldDTO {

    private String code;
    private String date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double diff;
    private String lastUpdate;
}
