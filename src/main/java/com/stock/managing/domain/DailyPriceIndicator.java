package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "daily_price_indicator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(DailyPriceIndicatorId.class)
public class DailyPriceIndicator {

    @Id
    @Column(name = "date", nullable = false)
    private String date;   // yyyy-MM-dd (DATE지만 String으로 저장 가능)

    @Id
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "change_amount")
    private Double changeAmount;

    @Column(name = "change_rate")
    private Double changeRate;

    @Column(name = "close")
    private Double close;

    @Column(name = "last_update")
    private String lastUpdate;   // DATETIME. 필요하면 LocalDateTime으로 변경 가능
}
