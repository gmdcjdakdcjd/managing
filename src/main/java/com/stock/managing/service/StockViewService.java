package com.stock.managing.service;

import com.stock.managing.domain.view.*;
import com.stock.managing.dto.PriceDTO;
import com.stock.managing.dto.StockDTO;
import com.stock.managing.repository.view.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 종목 기본정보 + 최근 시세를 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class StockViewService {

    private final VCompanyInfoRepository vCompanyInfoRepository;
    private final VCompanyInfoUsRepository vCompanyInfoUsRepository;
    private final VDailyPriceRepository vDailyPriceRepository;
    private final VDailyPriceUsRepository vDailyPriceUsRepository;

    /** 종목명 또는 종목코드로 정보 조회 */
    public StockDTO getStockInfo(String stockName, String stockCode) {

        Optional<VCompanyInfo> kor = Optional.empty();
        Optional<VCompanyInfoUs> us = Optional.empty();

        // ✅ 1️⃣ 기본정보 검색
        if (stockCode != null && !stockCode.isBlank()) {
            if (stockCode.matches("\\d+")) { // 숫자 → 한국
                kor = vCompanyInfoRepository.findById(stockCode);
            } else {
                us = vCompanyInfoUsRepository.findById(stockCode);
            }
        } else if (stockName != null && !stockName.isBlank()) {
            List<VCompanyInfo> korList = vCompanyInfoRepository.findByNameContaining(stockName);
            List<VCompanyInfoUs> usList = vCompanyInfoUsRepository.findByNameContaining(stockName);
            if (!korList.isEmpty()) kor = Optional.of(korList.get(0));
            else if (!usList.isEmpty()) us = Optional.of(usList.get(0));
        }

        // ✅ 2️⃣ 시세 데이터 (명시적 타입)
        List<PriceDTO> priceList = new ArrayList<>();
        String code = null;
        String name = null;
        String market = null;

        if (kor.isPresent()) {
            code = kor.get().getCode();
            name = kor.get().getName();
            market = "KOSPI";

            // ✅ DB 엔티티 → DTO 변환
            priceList = vDailyPriceRepository.findAllByCodeOrderByDateDesc(code)
                    .stream()
                    .map(p -> PriceDTO.builder()
                            .date(p.getDate())
                            .open(p.getOpen())
                            .high(p.getHigh())
                            .low(p.getLow())
                            .close(p.getClose())
                            .volume(p.getVolume())
                            .build())
                    .toList();

        } else if (us.isPresent()) {
            code = us.get().getCode();
            name = us.get().getName();
            market = "NASDAQ";

            // ✅ 미국 주식도 동일하게 변환
            priceList = vDailyPriceUsRepository.findAllByCodeOrderByDateDesc(code)
                    .stream()
                    .map(p -> PriceDTO.builder()
                            .date(p.getDate())
                            .open(p.getOpen())
                            .high(p.getHigh())
                            .low(p.getLow())
                            .close(p.getClose())
                            .volume(p.getVolume())
                            .build())
                    .toList();
        }

        if (code == null) return null;

        // ✅ 3️⃣ DTO 반환
        return StockDTO.builder()
                .code(code)
                .name(name)
                .marketType(market)
                .priceList(priceList)
                .build();
    }

}
