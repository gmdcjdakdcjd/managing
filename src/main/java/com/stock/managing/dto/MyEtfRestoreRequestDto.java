package com.stock.managing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyEtfRestoreRequestDto {

    private String etfName;

    /** 복구할 history id 목록 */
    private List<Long> historyIds;
}
