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
    private final StockViewService stockViewService;
    private final MyEtfHistoryRepository historyRepository;
    private final ExchangeRateService exchangeRateService;

    public List<MyEtfSummaryDto> getMyEtfList(String userId) {

        double usdRate = exchangeRateService.getUsdRate();

        // ETF 이름 목록만 가져온다 (distinct)
        List<String> etfNames =
                etfItemRepository.findDistinctEtfNameByUserId(userId);

        List<MyEtfSummaryDto> result = new ArrayList<>();

        for (String etfName : etfNames) {

            List<MyEtfItemEntity> items =
                    etfItemRepository.findByUserIdAndEtfNameAndDeletedYn(
                            userId, etfName, "N"
                    );

            long itemCount = items.size();
            long investedAmount = 0;
            long evaluatedAmount = 0;

            for (MyEtfItemEntity item : items) {

                boolean isUsStock =
                        item.getCode() != null &&
                                item.getCode().matches(".*[A-Za-z].*");

                // 🔹 투자금 (편입가 기준)
                if (item.getPriceAtAdd() != null) {
                    double price = item.getPriceAtAdd();
                    if (isUsStock) price *= usdRate;
                    investedAmount += Math.round(price * item.getQuantity());
                }

                // 🔹 평가금 (현재가 기준)
                StockDTO stock = stockViewService.getStockInfo(null, item.getCode());
                if (stock == null || stock.getPriceList().isEmpty()) continue;

                double currentPrice = stock.getPriceList().get(0).getClose();
                if (isUsStock) currentPrice *= usdRate;

                evaluatedAmount += Math.round(currentPrice * item.getQuantity());
            }

            double profitRate =
                    investedAmount > 0
                            ? ((evaluatedAmount - investedAmount) * 100.0) / investedAmount
                            : 0.0;

            result.add(
                    MyEtfSummaryDto.builder()
                            .etfName(etfName)
                            .itemCount(itemCount)
                            .investedAmount((double) investedAmount)
                            .evaluatedAmount((double) evaluatedAmount)
                            .profitRate(profitRate)
                            .build()
            );
        }

        return result;
    }



    @Transactional
    public void createEtf(
            String userId,
            MyEtfCreateRequestDto request
    ) {
        for (MyEtfItemRequestDto item : request.getItems()) {

            // 현재가 조회
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



    public List<MyEtfItemDto> getEtfItemList(String userId, String etfName) {

        List<MyEtfItemEntity> items =
                etfItemRepository.findByUserIdAndEtfNameAndDeletedYn(
                        userId, etfName, "N"
                );

        List<MyEtfItemDto> result = new ArrayList<>();

        for (MyEtfItemEntity item : items) {

            MyEtfItemDto dto = MyEtfItemDto.fromEntity(item);

            // 🔴 현재가 조회
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





    public String getEtfDescription(String userId, String etfName) {
        return etfItemRepository
                .findFirstByUserIdAndEtfNameAndDeletedYn(userId, etfName, "N")
                .map(MyEtfItemEntity::getEtfDescription)
                .orElse(null);
    }

    @Transactional
    public void editEtf(String userId, MyEtfEditRequestDto request) {

        for (MyEtfEditItemDto dto : request.getItems()) {

            // =========================
            // 1️⃣ 신규 종목 추가
            // =========================
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

            // =========================
            // 2️⃣ 기존 종목 처리
            // =========================
            MyEtfItemEntity entity = etfItemRepository
                    .findById(dto.getId())
                    .orElseThrow(() -> new IllegalStateException("ETF 종목 없음"));

            // 🔥 삭제 처리
            if (dto.isDeleted()) {
                entity.setDeletedYn("Y");
                entity.setDeletedAt(LocalDateTime.now());

                historyRepository.save(
                        MyEtfItemHistoryEntity.fromEntity(entity)
                );
                continue;
            }

            // 🔥 수량 변경
            if (!entity.getQuantity().equals(dto.getQuantity())) {
                entity.setQuantity(dto.getQuantity());
            }
        }
    }


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

            // 🔥 여기 핵심
            hist.markRestored();
        }
    }

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

            // 🔹 편입금액
            double investedPrice = item.getPriceAtAdd();
            if (isUsStock) investedPrice *= usdRate;

            totalInvested += Math.round(investedPrice * item.getQuantity());

            // 🔹 현재가
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






}
