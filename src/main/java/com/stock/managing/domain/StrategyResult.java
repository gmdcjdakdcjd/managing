package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "strategy_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StrategyResultId.class)
public class StrategyResult {

    @Id
    @Column(name = "strategy_name", nullable = false, length = 50)
    private String strategyName;

    @Id
    @Column(name = "signal_date", nullable = false)
    private String signalDate;     // DATE → String or LocalDate도 가능

    @Column(name = "signal_type")
    private String signalType;

    @Column(name = "total_data")
    private Integer totalData;

    @Column(name = "created_at")
    private String createdAt;      // DATETIME
}
