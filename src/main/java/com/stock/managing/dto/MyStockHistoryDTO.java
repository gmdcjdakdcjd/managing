package com.stock.managing.dto;

import com.stock.managing.domain.MyStockHistory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyStockHistoryDTO {

    private Long histId;
    private Long myStockId;

    private String userId;
    private String code;
    private String name;

    private String action;          // ADD / UPDATE / DELETE / RESTORE
    private String strategyName;

    private Double specialValue;
    private Double priceAtAdd;
    private Double targetPrice5;
    private Double targetPrice10;

    private String memo;
    private LocalDateTime createdAt;

    private String note;

    // ============================
    // Entity → DTO 변환
    // ============================
    public static MyStockHistoryDTO fromEntity(MyStockHistory entity) {
        return MyStockHistoryDTO.builder()
                .histId(entity.getHistId())
                .myStockId(entity.getMyStockId())
                .userId(entity.getUserId())
                .code(entity.getCode())
                .name(entity.getName())
                .action(entity.getAction())
                .strategyName(entity.getStrategyName())
                .specialValue(entity.getSpecialValue())
                .priceAtAdd(entity.getPriceAtAdd())
                .targetPrice5(entity.getTargetPrice5())
                .targetPrice10(entity.getTargetPrice10())
                .memo(entity.getMemo())
                .createdAt(entity.getCreatedAt())
                .note(entity.getNote())
                .build();
    }

    // ============================
    // DTO → Entity 변환
    // ============================
    public MyStockHistory toEntity() {
        return MyStockHistory.builder()
                .histId(histId)
                .myStockId(myStockId)
                .userId(userId)
                .code(code)
                .name(name)
                .action(action)
                .strategyName(strategyName)
                .specialValue(specialValue)
                .priceAtAdd(priceAtAdd)
                .targetPrice5(targetPrice5)
                .targetPrice10(targetPrice10)
                .memo(memo)
                .createdAt(createdAt)
                .note(note)
                .build();
    }
}
