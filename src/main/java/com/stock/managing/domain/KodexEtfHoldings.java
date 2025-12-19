package com.stock.managing.entity;

import com.stock.managing.domain.KodexEtfHoldingsId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kodex_etf_holdings", schema = "managing")
public class KodexEtfHoldings {

    @EmbeddedId
    private KodexEtfHoldingsId id;

    @Column(name = "stock_name", length = 100, nullable = false)
    private String stockName;

    @Column(name = "holding_qty", precision = 20, scale = 6)
    private BigDecimal holdingQty;

    @Column(name = "current_price")
    private Integer currentPrice;

    @Column(name = "eval_amount", nullable = false)
    private Long evalAmount;

    @Column(name = "weight_ratio", precision = 10, scale = 4)
    private BigDecimal weightRatio;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
