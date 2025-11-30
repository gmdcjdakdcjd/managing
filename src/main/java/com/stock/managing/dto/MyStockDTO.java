package com.stock.managing.dto;

import com.stock.managing.domain.MyStock;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyStockDTO {

    private Long id;
    private String userId;
    private String code;
    private String name;
    private String strategyName;
    private Double specialValue;
    private Double priceAtAdd;
    private Double targetPrice5;
    private Double targetPrice10;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deletedYn;
    private LocalDateTime deletedAt;

    private Double currentPrice;

    // ============================
    // 🔄 Entity → DTO 변환
    // ============================
    public static MyStockDTO fromEntity(MyStock entity) {
        return MyStockDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .code(entity.getCode())
                .name(entity.getName())
                .strategyName(entity.getStrategyName())
                .specialValue(entity.getSpecialValue())
                .priceAtAdd(entity.getPriceAtAdd())
                .targetPrice5(entity.getTargetPrice5())
                .targetPrice10(entity.getTargetPrice10())
                .memo(entity.getMemo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedYn(entity.getDeletedYn())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    // ============================
    // 🔄 DTO → Entity 변환
    // ============================
    public MyStock toEntity() {
        return MyStock.builder()
                .id(id)
                .userId(userId)
                .code(code)
                .name(name)
                .strategyName(strategyName)
                .specialValue(specialValue)
                .priceAtAdd(priceAtAdd)
                .targetPrice5(targetPrice5)
                .targetPrice10(targetPrice10)
                .memo(memo)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedYn(deletedYn)
                .deletedAt(deletedAt)
                .build();
    }
}
