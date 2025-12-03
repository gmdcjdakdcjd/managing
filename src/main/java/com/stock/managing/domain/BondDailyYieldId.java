package com.stock.managing.domain;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BondDailyYieldId implements Serializable {

    private String code;
    private LocalDate date;
}
