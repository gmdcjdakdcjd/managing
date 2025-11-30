package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "strategy_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StrategyDetailId.class)
public class StrategyDetail {

    @Id
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Id
    @Column(name = "signal_date", nullable = false)
    private String signalDate;   // DATE → String 또는 LocalDate 가능

    @Id
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "result_id")
    private String resultId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long  price;

    @Column(name = "prev_close")
    private Long  prevClose;

    @Column(name = "diff")
    private Long  diff;

    @Column(name = "volume")
    private Long  volume;

    @Column(name = "special_value")
    private Integer specialValue;

    @Column(name = "created_at")
    private String createdAt;  // DATETIME → String or LocalDateTime
}
