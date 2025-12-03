package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

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
    private LocalDate signalDate;     // DATE → String or LocalDate도 가능

    @Column(name = "signal_type")
    private String signalType;

    @Column(name = "total_data")
    private Integer totalData;

    @Column(name = "created_at")
    private Date createdAt;      // DATETIME
}
