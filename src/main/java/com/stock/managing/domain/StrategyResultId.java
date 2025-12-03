package com.stock.managing.domain;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResultId implements Serializable {

    private String strategyName;
    private LocalDate signalDate;
}
