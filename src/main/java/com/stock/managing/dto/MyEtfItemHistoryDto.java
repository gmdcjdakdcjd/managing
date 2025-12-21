package com.stock.managing.dto;

import com.stock.managing.domain.MyEtfItemHistoryEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyEtfItemHistoryDto {

    private Long histId;
    private Long etfItemId;

    private String code;
    private String name;

    private String etfName;
    private String etfDescription;

    private Integer quantity;
    private Double priceAtAdd;

    private String memo;
    private LocalDateTime deletedAt;

    public static MyEtfItemHistoryDto fromEntity(MyEtfItemHistoryEntity entity) {
        return MyEtfItemHistoryDto.builder()
                .histId(entity.getHistId())
                .etfItemId(entity.getEtfItemId())
                .code(entity.getCode())
                .name(entity.getName())
                .etfName(entity.getEtfName())
                .etfDescription(entity.getEtfDescription())
                .quantity(entity.getQuantity())
                .priceAtAdd(entity.getPriceAtAdd())
                .memo(entity.getMemo())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
