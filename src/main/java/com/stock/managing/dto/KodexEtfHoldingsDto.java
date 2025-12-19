package com.stock.managing.dto;

import com.stock.managing.entity.KodexEtfHoldings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class KodexEtfHoldingsDto {

    private String etfId;
    private String baseDate;
    private String stockCode;
    private String stockName;
    private BigDecimal holdingQty;
    private Integer currentPrice;
    private Long evalAmount;
    private BigDecimal weightRatio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KodexEtfHoldingsDto fromEntity(KodexEtfHoldings entity) {
        if (entity == null) {
            return null;
        }

        KodexEtfHoldingsDto dto = new KodexEtfHoldingsDto();

        if (entity.getId() != null) {
            dto.setEtfId(entity.getId().getEtfId());
            dto.setBaseDate(entity.getId().getBaseDate());
            dto.setStockCode(entity.getId().getStockCode());
        }

        dto.setStockName(entity.getStockName());
        dto.setHoldingQty(entity.getHoldingQty());
        dto.setCurrentPrice(entity.getCurrentPrice());
        dto.setEvalAmount(entity.getEvalAmount());
        dto.setWeightRatio(entity.getWeightRatio());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
