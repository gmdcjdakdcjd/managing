package com.stock.managing.dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyResultDTO {

    private String strategyName;
    private LocalDate signalDate;
    private String signalType;
    private Integer totalData;
    private Date createdAt;
}
