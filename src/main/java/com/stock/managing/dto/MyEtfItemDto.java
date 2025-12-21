package com.stock.managing.dto;

import com.stock.managing.domain.MyEtfItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MyEtfItemDto {

    private Long id;

    private String code;
    private String name;

    private String etfName;
    private String etfDescription;

    private Integer quantity;

    private Double priceAtAdd;       // 편입가
    private Double currentPrice;     // 현재가 (계산)
    private Double evaluatedAmount;  // 평가금액 (currentPrice * quantity)

    private String memo;
    private String priceAtAddDisplay;

    private String currentPriceDisplay;     // 현재가 표시용
    private String evaluatedAmountDisplay;  // 평가금액 표시용

    private Double profitRate;          // 수익률 (숫자)
    private String profitRateDisplay;   // 수익률 표시용

    public static MyEtfItemDto fromEntity(MyEtfItemEntity entity) {
        return MyEtfItemDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .etfName(entity.getEtfName())
                .etfDescription(entity.getEtfDescription())
                .quantity(entity.getQuantity())
                .priceAtAdd(entity.getPriceAtAdd())
                .memo(entity.getMemo())
                .build();
    }
}
