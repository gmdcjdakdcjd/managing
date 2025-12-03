package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bond_daily_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BondDailyYieldId.class)
public class BondDailyYield {

    @Id
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;   // yyyy-MM-dd로 저장한다고 했으니까 String 유지

    @Column(name = "open")
    private Double open;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;

    @Column(name = "close")
    private Double close;

    @Column(name = "diff")
    private Double diff;

    @Column(name = "last_update")
    private String lastUpdate;  // DATETIME
}
