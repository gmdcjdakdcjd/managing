package com.stock.managing.service;

import com.stock.managing.domain.MyEtfItemEntity;
import com.stock.managing.domain.MyEtfItemHistoryEntity;
import com.stock.managing.dto.*;
import com.stock.managing.repository.MyEtfHistoryRepository;
import com.stock.managing.repository.MyEtfItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyEtfService {

    private final MyEtfItemRepository etfItemRepository;
    private final MyEtfHistoryRepository historyRepository;
    private final StockViewService stockViewService;
    private final ExchangeRateService exchangeRateService;

    /* =========================
       ETF 목록 요약
       ========================= */
    public List<MyEtfSummaryDto> getMyEtfList(String userId) {

        double usdRate = exchangeRateService.getUsdRate();
        List<Object[]> rows = etfItemRepository.findMyEtfSummary(userId);
        List<MyEtfSummaryDto> result = new ArrayList<>();

        for (Object[] r : rows) {

            String etfName = (String) r[0];
            String etfDescription = (String) r[1];
            long itemCount = (long) r[2];
            double investedAmount = ((Number) r[3]).doubleValue();

            double evaluatedAmount = 0;

            List<MyEtfItemEntity> items =
                    etfItemRepository.findByUserIdAndEtfNameAndDeletedYn(
                            userId, etfName, "N"
                    );

            for (MyEtfItemEntity item : items) {

                boolean isUsStock =
                        item.getCode() != null &&
                                item.getCode().matches(".*[A-Za-z].*");

                StockDTO stock =
                        stockViewService.getStockInfo(null, item.getCode());
                if (stock == null || stock.getPriceList().isEmpty()) continue;

                double price = stock.getPriceList().get(0).getClose();
                if (isUsStock) price *= usdRate;

                evaluatedAmount += price * item.getQuantity();
            }

            double profitRate =
                    investedAmount > 0
                            ? ((evaluatedAmount - investedAmount) * 100.0) / investedAmount
                            : 0.0;

            result.add(
                    MyEtfSummaryDto.builder()
                            .etfName(etfName)
                            .etfDescription(etfDescription)
                            .itemCount(itemCount)
                            .investedAmount(investedAmount)
                            .evaluatedAmount(evaluatedAmount)
                            .profitRate(profitRate)
                            .build()
            );
        }

        return result;
    }

    /* =========================
       ETF 생성
       ========================= */
    @Transactional
    public void createEtf(String userId, MyEtfCreateRequestDto request) {

        for (MyEtfItemRequestDto item : request.getItems()) {

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            Double priceAtAdd = null;

            if (stock != null && !stock.getPriceList().isEmpty()) {
                priceAtAdd = stock.getPriceList().get(0).getClose();
            }

            MyEtfItemEntity entity = MyEtfItemEntity.builder()
                    .userId(userId)
                    .code(item.getCode())
                    .name(item.getName())
                    .etfName(request.getEtfName())
                    .etfDescription(request.getEtfDescription())
                    .quantity(item.getQuantity())
                    .priceAtAdd(priceAtAdd)
                    .memo(item.getMemo())
                    .deletedYn("N")
                    .build();

            etfItemRepository.save(entity);
        }
    }

    /* =========================
       ETF 상세 종목
       ========================= */
    public List<MyEtfItemDto> getEtfItemList(String userId, String etfName) {

        List<MyEtfItemEntity> items =
                etfItemRepository.findByUserIdAndEtfNameAndDeletedYn(
                        userId, etfName, "N"
                );

        List<MyEtfItemDto> result = new ArrayList<>();

        for (MyEtfItemEntity item : items) {

            MyEtfItemDto dto = MyEtfItemDto.fromEntity(item);

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            if (stock == null || stock.getPriceList().isEmpty()) {
                dto.setCurrentPrice(null);
                dto.setEvaluatedAmount(null);
            } else {
                double currentPrice = stock.getPriceList().get(0).getClose();
                dto.setCurrentPrice(currentPrice);
                dto.setEvaluatedAmount(currentPrice * item.getQuantity());
            }

            result.add(dto);
        }

        return result;
    }

    /* =========================
       ETF 설명 조회
       ========================= */
    public String getEtfDescription(String userId, String etfName) {
        return etfItemRepository
                .findFirstByUserIdAndEtfNameAndDeletedYn(userId, etfName, "N")
                .map(MyEtfItemEntity::getEtfDescription)
                .orElse(null);
    }

    /* =========================
       ETF 편집 (설명 + 종목 추가/삭제)
       ========================= */
    @Transactional
    public void editEtf(String userId, MyEtfEditRequestDto request) {

        // ✅ 1. ETF 설명 수정 (메타 정보)
        if (request.getEtfDescription() != null) {
            etfItemRepository.updateEtfDescription(
                    userId,
                    request.getEtfName(),
                    request.getEtfDescription()
            );
        }

        // ✅ 2. ETF 구성 종목 처리
        for (MyEtfEditItemDto dto : request.getItems()) {

            // 신규 종목 추가
            if (dto.getId() == null && !dto.isDeleted()) {

                StockDTO stock = stockViewService.getStockInfo(null, dto.getCode());
                Double priceAtAdd = null;

                if (stock != null && !stock.getPriceList().isEmpty()) {
                    priceAtAdd = stock.getPriceList().get(0).getClose();
                }

                MyEtfItemEntity newEntity = MyEtfItemEntity.builder()
                        .userId(userId)
                        .code(dto.getCode())
                        .name(dto.getName())
                        .etfName(request.getEtfName())
                        .quantity(dto.getQuantity())
                        .priceAtAdd(priceAtAdd)
                        .deletedYn("N")
                        .build();

                etfItemRepository.save(newEntity);
                continue;
            }

            // 기존 종목
            MyEtfItemEntity entity = etfItemRepository
                    .findById(dto.getId())
                    .orElseThrow(() -> new IllegalStateException("ETF 종목 없음"));

            // 삭제만 허용
            if (dto.isDeleted()) {
                entity.setDeletedYn("Y");
                entity.setDeletedAt(LocalDateTime.now());

                historyRepository.save(
                        MyEtfItemHistoryEntity.fromEntity(entity)
                );
            }
        }
    }

    /* =========================
       ETF 종목 복구
       ========================= */
    @Transactional
    public void restoreEtfItems(String userId, MyEtfRestoreRequestDto request) {

        for (Long histId : request.getHistoryIds()) {

            MyEtfItemHistoryEntity hist = historyRepository.findById(histId)
                    .orElseThrow(() -> new IllegalStateException("복구 대상 없음"));

            MyEtfItemEntity item = etfItemRepository
                    .findByUserIdAndEtfNameAndCode(
                            userId,
                            hist.getEtfName(),
                            hist.getCode()
                    )
                    .orElse(null);

            if (item == null) {
                item = MyEtfItemEntity.builder()
                        .userId(userId)
                        .code(hist.getCode())
                        .name(hist.getName())
                        .etfName(hist.getEtfName())
                        .etfDescription(hist.getEtfDescription())
                        .priceAtAdd(hist.getPriceAtAdd())
                        .quantity(hist.getQuantity())
                        .memo(hist.getMemo())
                        .deletedYn("N")
                        .build();
            } else {
                item.setDeletedYn("N");
                item.setDeletedAt(null);
                item.setQuantity(hist.getQuantity());
            }

            etfItemRepository.save(item);
            hist.markRestored();
        }
    }

    /* =========================
       ETF 상세 요약
       ========================= */
    public MyEtfDetailSummaryDto getEtfDetailSummary(String userId, String etfName) {

        double usdRate = exchangeRateService.getUsdRate();
        long totalInvested = 0;
        long totalEvaluated = 0;

        List<MyEtfItemEntity> items =
                etfItemRepository.findByUserIdAndEtfNameAndDeletedYn(
                        userId, etfName, "N"
                );

        for (MyEtfItemEntity item : items) {

            if (item.getPriceAtAdd() == null) continue;

            boolean isUsStock =
                    item.getCode() != null &&
                            item.getCode().matches(".*[A-Za-z].*");

            double investedPrice = item.getPriceAtAdd();
            if (isUsStock) investedPrice *= usdRate;

            totalInvested += Math.round(investedPrice * item.getQuantity());

            StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
            if (stock == null || stock.getPriceList().isEmpty()) continue;

            double currentPrice = stock.getPriceList().get(0).getClose();
            if (isUsStock) currentPrice *= usdRate;

            totalEvaluated += Math.round(currentPrice * item.getQuantity());
        }

        long profitAmount = totalEvaluated - totalInvested;
        double profitRate =
                totalInvested > 0
                        ? (profitAmount * 100.0) / totalInvested
                        : 0.0;

        return MyEtfDetailSummaryDto.builder()
                .totalInvested(totalInvested)
                .totalEvaluated(totalEvaluated)
                .profitAmount(profitAmount)
                .profitRate(profitRate)
                .build();
    }

    /* =========================
       ETF 삭제
       ========================= */
    @Transactional
    public void deleteEtf(String userId, String etfName) {
        etfItemRepository.softDeleteByUserIdAndEtfName(userId, etfName);
    }
}
