package com.stock.managing.domain;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResultId implements Serializable {

    private String strategyName;
    private String signalDate;
}
