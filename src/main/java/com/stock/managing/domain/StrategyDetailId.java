package com.stock.managing.domain;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDetailId implements Serializable {

    private String code;
    private LocalDate signalDate;
    private String action;
}
