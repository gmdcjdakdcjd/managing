package com.stock.managing.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPriceIndicatorId implements Serializable {

    private LocalDate date;
    private String code;
}
