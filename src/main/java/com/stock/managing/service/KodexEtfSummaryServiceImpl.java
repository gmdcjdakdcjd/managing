package com.stock.managing.service;

import com.stock.managing.domain.KodexEtfSummary;
import com.stock.managing.dto.KodexEtfSummaryDto;

import com.stock.managing.repository.KodexEtfHoldingsRepository;
import com.stock.managing.repository.KodexEtfSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KodexEtfSummaryServiceImpl implements KodexEtfSummaryService {

    private final KodexEtfSummaryRepository summaryRepository;
    private final KodexEtfHoldingsRepository holdingsRepository;

    @Override
    public List<KodexEtfSummaryDto> getAllSummaryList() {
        return summaryRepository.findAll()
                .stream()
                .map(KodexEtfSummaryDto::fromEntity)
                .toList();
    }

    @Override
    public List<KodexEtfSummaryDto> search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllSummaryList();
        }

        String stockCode = null;
        String stockName = null;

        // ✅ 종목코드(숫자 6자리) 판별
        if (keyword.matches("\\d{6}")) {
            stockCode = keyword;
        } else {
            stockName = keyword;
        }

        // 1. 종목 기준으로 ETF 후보 찾기
        List<String> etfIds =
                holdingsRepository.findEtfsByStock(stockCode, stockName)
                        .stream()
                        .map(h -> h.getId().getEtfId())
                        .distinct()
                        .toList();

        // 2. 종목 검색 결과가 있으면 → summary 기준 출력
        if (!etfIds.isEmpty()) {
            return summaryRepository.findById_EtfIdIn(etfIds)
                    .stream()
                    .map(KodexEtfSummaryDto::fromEntity)
                    .toList();
        }

        // 3. 없으면 → ETF명 / ETF코드 검색
        return summaryRepository
                .findByEtfNameContainingIgnoreCaseOrId_EtfIdContainingIgnoreCase(
                        keyword, keyword
                )
                .stream()
                .map(KodexEtfSummaryDto::fromEntity)
                .toList();
    }

}