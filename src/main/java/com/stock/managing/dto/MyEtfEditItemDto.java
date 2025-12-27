package com.stock.managing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyEtfEditItemDto {

    /** 기존 ETF 종목이면 존재, 신규면 null */
    private Long id;

    /** 종목 코드 */
    private String code;

    /** 종목명 */
    private String name;

    /** 수량 */
    private Integer quantity;

    /** 삭제 여부 (true면 soft delete) */
    private boolean deleted;
}
