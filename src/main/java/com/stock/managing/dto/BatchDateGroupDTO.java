package com.stock.managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class BatchDateGroupDTO {

    private LocalDate date;   // 2025-11-30 같은 실제 날짜
    private String dateKey;   // "20251130" ← 템플릿에서 id로 사용
    private List<?> items;    // IN/OUT 이력 리스트
}
