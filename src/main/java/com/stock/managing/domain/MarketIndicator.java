package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "market_indicator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String date;  // yyyy-MM-dd

    private Double close;

    @Column(name = "change_amount")
    private Double changeAmount;

    @Column(name = "change_rate")
    private Double changeRate;
}