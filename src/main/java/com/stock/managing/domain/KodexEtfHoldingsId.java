package com.stock.managing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class KodexEtfHoldingsId implements Serializable {

    @Column(name = "etf_id", length = 20)
    private String etfId;

    @Column(name = "base_date", length = 8)
    private String baseDate;

    @Column(name = "stock_code", length = 20)
    private String stockCode;

    public KodexEtfHoldingsId(String etfId, String baseDate, String stockCode) {
        this.etfId = etfId;
        this.baseDate = baseDate;
        this.stockCode = stockCode;
    }
}
