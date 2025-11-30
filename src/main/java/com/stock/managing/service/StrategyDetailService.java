package com.stock.managing.service;

import com.stock.managing.domain.StrategyDetail;
import com.stock.managing.domain.StrategyResult;
import com.stock.managing.dto.StrategyDetailDTO;
import com.stock.managing.repository.StrategyDetailRepository;
import com.stock.managing.repository.StrategyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyDetailService {

    private final StrategyResultRepository resultRepository;
    private final StrategyDetailRepository detailRepository;

    public List<StrategyDetailDTO> getLatestOrToday(String strategyName, String today) {

        // 1) 기본적으로 targetDate = today
        String targetDate = today;

        // 2) 미국 전략이면 날짜 -1일
        if (strategyName.endsWith("_US")) {
            targetDate = LocalDate.parse(today).minusDays(1).toString();
        }

        // 3) 해당 날짜 SUMMARY가 있는지 검사
        boolean exists = resultRepository.existsByStrategyNameAndSignalDate(strategyName, targetDate);

        // 4) 없으면 → 최신 날짜로 fallback
        if (!exists) {
            StrategyResult latest = resultRepository
                    .findTopByStrategyNameOrderBySignalDateDesc(strategyName);
            if (latest == null) return List.of();

            targetDate = latest.getSignalDate();
        }

        // 5) DETAIL 조회
        return detailRepository
                .findByActionAndSignalDateOrderBySpecialValueAsc
                        (strategyName, targetDate)
                .stream()
                .map(this::toDTO)
                .toList();
    }



    // Entity → DTO 변환
    private StrategyDetailDTO toDTO(StrategyDetail d) {
        return StrategyDetailDTO.builder()
                .resultId(d.getResultId())
                .signalDate(d.getSignalDate())
                .code(d.getCode())
                .name(d.getName())
                .action(d.getAction())
                .price(d.getPrice())
                .prevClose(d.getPrevClose())
                .diff(d.getDiff())
                .volume(d.getVolume())
                .specialValue(d.getSpecialValue())
                .createdAt(d.getCreatedAt())
                .build();
    }

    public List<StrategyDetailDTO> searchDetail(String keyword) {
        return detailRepository.findByKeyword(keyword);
    }

    public List<StrategyDetailDTO> getDetail(String strategy, String date) {
        return detailRepository
                .findByActionAndSignalDateOrderBySpecialValueAsc(strategy, date)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
