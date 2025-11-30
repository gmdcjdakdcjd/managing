package com.stock.managing.service;

import com.stock.managing.domain.StrategyResult;
import com.stock.managing.dto.PageRequestDTO;
import com.stock.managing.dto.PageResponseDTO;
import com.stock.managing.dto.StrategyResultDTO;
import com.stock.managing.repository.StrategyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyResultService {

    private final StrategyResultRepository resultRepository;

    // ============================
    // 🇰🇷 KR 전략 리스트 조회
    // ============================
    public List<String> getKRStrategyList() {
        return List.of(
                "DAILY_120D_NEW_HIGH_KR",
                "DAILY_120D_NEW_LOW_KR",
                "DAILY_BB_LOWER_TOUCH_KR",
                "DAILY_BB_UPPER_TOUCH_KR",
                "DAILY_TOUCH_MA60_KR",
                "RSI_30_UNHEATED_KR",
                "RSI_70_OVERHEATED_KR",
                "WEEKLY_52W_NEW_HIGH_KR",
                "WEEKLY_52W_NEW_LOW_KR",
                "WEEKLY_TOUCH_MA60_KR"
        );
    }

    // ============================
    // 🇰🇷 US 전략 리스트 조회
    // ============================
    public List<String> getUSStrategyList() {
        return List.of(
                "DAILY_120D_NEW_HIGH_US",
                "DAILY_120D_NEW_LOW_US",
                "DAILY_BB_LOWER_TOUCH_US",
                "DAILY_BB_UPPER_TOUCH_US",
                "DAILY_TOUCH_MA60_US",
                "RSI_30_UNHEATED_US",
                "RSI_70_OVERHEATED_US",
                "WEEKLY_52W_NEW_HIGH_US",
                "WEEKLY_52W_NEW_LOW_US",
                "WEEKLY_TOUCH_MA60_US"
        );
    }

    // ============================
    // 🇰🇷 KR 필터 조회
    // ============================
    public PageResponseDTO<StrategyResultDTO> listKR(
            PageRequestDTO dto, String strategy, String regDate) {

        Pageable pageable = dto.getPageable("signalDate");

        Page<StrategyResult> resultPage;

        if (strategy != null && !strategy.isEmpty()
                && regDate != null && !regDate.isEmpty()) {

            resultPage = resultRepository
                    .findByStrategyNameAndSignalDate(strategy, regDate, pageable);

        } else if (strategy != null && !strategy.isEmpty()) {

            resultPage = resultRepository
                    .findByStrategyName(strategy, pageable);

        } else if (regDate != null && !regDate.isEmpty()) {

            resultPage = resultRepository
                    .findBySignalDate(regDate, pageable);

        } else {

            resultPage = resultRepository
                    .findByStrategyNameEndingWith("_KR", pageable);
        }

        List<StrategyResultDTO> dtoList = resultPage.stream()
                .map(this::toDTO)
                .toList();

        return PageResponseDTO.<StrategyResultDTO>withAll()
                .pageRequestDTO(dto)
                .dtoList(dtoList)
                .total((int) resultPage.getTotalElements())
                .build();
    }

    // ============================
    // 🇰🇷 US 필터 조회
    // ============================
    public PageResponseDTO<StrategyResultDTO> listUS(
            PageRequestDTO dto, String strategy, String regDate) {

        Pageable pageable = dto.getPageable("signalDate");

        Page<StrategyResult> resultPage;

        if (strategy != null && !strategy.isEmpty()
                && regDate != null && !regDate.isEmpty()) {

            resultPage = resultRepository
                    .findByStrategyNameAndSignalDate(strategy, regDate, pageable);

        } else if (strategy != null && !strategy.isEmpty()) {

            resultPage = resultRepository
                    .findByStrategyName(strategy, pageable);

        } else if (regDate != null && !regDate.isEmpty()) {

            resultPage = resultRepository
                    .findBySignalDate(regDate, pageable);

        } else {

            resultPage = resultRepository
                    .findByStrategyNameEndingWith("_US", pageable);
        }

        List<StrategyResultDTO> dtoList = resultPage.stream()
                .map(this::toDTO)
                .toList();

        return PageResponseDTO.<StrategyResultDTO>withAll()
                .pageRequestDTO(dto)
                .dtoList(dtoList)
                .total((int) resultPage.getTotalElements())
                .build();
    }

    // ============================
    // 공통 DTO 변환
    // ============================
    private StrategyResultDTO toDTO(StrategyResult r) {
        return StrategyResultDTO.builder()
                .strategyName(r.getStrategyName())
                .signalDate(r.getSignalDate())
                .signalType(r.getSignalType())
                .totalData(r.getTotalData())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
