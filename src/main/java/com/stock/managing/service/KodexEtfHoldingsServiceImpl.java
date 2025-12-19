package com.stock.managing.service;

import com.stock.managing.dto.KodexEtfHoldingsDto;
import com.stock.managing.entity.KodexEtfHoldings;
import com.stock.managing.repository.KodexEtfHoldingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KodexEtfHoldingsServiceImpl implements KodexEtfHoldingsService {

    private final KodexEtfHoldingsRepository holdingsRepository;

    /**
     * ETF ID 기준 구성 종목 조회 (모달용)
     * - weightRatio NULL 우선
     * - 이후 DESC
     */
    @Override
    public List<KodexEtfHoldingsDto> getHoldingsByEtfId(String etfId) {

        return holdingsRepository
                .findHoldingsOrderByWeightRatioDescNullFirst(etfId)
                .stream()
                .map(KodexEtfHoldingsDto::fromEntity)
                .toList();
    }

    /**
     * 종목 검색 → 포함된 ETF 조회
     * - stockCode 또는 stockName 중 하나만 있어도 동작
     * - 한국 주식 전용
     */
    @Override
    public List<KodexEtfHoldingsDto> getEtfsByStock(String stockCode, String stockName) {

        // 둘 다 없으면 조회 의미 없음
        if ((stockCode == null || stockCode.isBlank())
                && (stockName == null || stockName.isBlank())) {
            return List.of();
        }

        String resolvedStockCode =
                (stockCode != null && !stockCode.isBlank()) ? stockCode : null;

        String resolvedStockName =
                (stockName != null && !stockName.isBlank()) ? stockName : null;

        return holdingsRepository
                .findEtfsByStock(resolvedStockCode, resolvedStockName)
                .stream()
                .map(KodexEtfHoldingsDto::fromEntity)
                .toList();
    }
}
