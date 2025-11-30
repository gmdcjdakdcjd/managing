package com.stock.managing.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPriceIndicatorId implements Serializable {

    private String date;
    private String code;
}
