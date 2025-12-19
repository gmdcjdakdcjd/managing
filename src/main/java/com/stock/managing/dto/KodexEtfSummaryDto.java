package com.stock.managing.dto;

import com.stock.managing.domain.KodexEtfSummary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class KodexEtfSummaryDto {

    private String etfId;
    private String baseDate;
    private String etfName;
    private String irpYn;
    private Integer totalCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static KodexEtfSummaryDto fromEntity(KodexEtfSummary entity) {
        KodexEtfSummaryDto dto = new KodexEtfSummaryDto();
        dto.setEtfId(entity.getId().getEtfId());
        dto.setBaseDate(entity.getId().getBaseDate());
        dto.setEtfName(entity.getEtfName());
        dto.setIrpYn(entity.getIrpYn());
        dto.setTotalCnt(entity.getTotalCnt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

}
