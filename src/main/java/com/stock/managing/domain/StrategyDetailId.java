package com.stock.managing.domain;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDetailId implements Serializable {

    private String code;
    private String signalDate;
    private String action;
}
