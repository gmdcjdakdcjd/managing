package com.stock.managing.service;

import com.stock.managing.dto.KodexEtfHoldingsDto;

import java.util.List;

public interface KodexEtfHoldingsService {

    /**
     * ETF ID 기준 구성 종목 전체 조회 (모달용)
     */
    List<KodexEtfHoldingsDto> getHoldingsByEtfId(String etfId);


    // 종목 → ETF (입력은 code or name)
    List<KodexEtfHoldingsDto> getEtfsByStock(String stockCode, String stockName);
}
