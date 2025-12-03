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

        // 기존 로직 그대로
        LocalDate targetDate = LocalDate.parse(today);

        if (strategyName.endsWith("_US")) {
            targetDate = targetDate.minusDays(1);
        }

        boolean exists = resultRepository.existsByStrategyNameAndSignalDate(strategyName, targetDate);

        if (!exists) {
            StrategyResult latest = resultRepository
                    .findTopByStrategyNameOrderBySignalDateDesc(strategyName);
            if (latest == null) return List.of();

            targetDate = latest.getSignalDate();
        }

        List<StrategyDetailDTO> list = detailRepository
                .findByActionAndSignalDateOrderBySpecialValueAsc(strategyName, targetDate)
                .stream()
                .map(this::toDTO)
                .toList();

        // 🔥🔥 여기서 KR/US 구분해 가격 포맷 처리 🔥🔥
        return formatPrice(strategyName, list);
    }

    private List<StrategyDetailDTO> formatPrice(String strategyName, List<StrategyDetailDTO> list) {

        boolean isKR = strategyName.endsWith("_KR");
        boolean isUS = strategyName.endsWith("_US");

        for (StrategyDetailDTO s : list) {

            if (isKR) {
                s.setPrice( floor0(s.getPrice()) );
                s.setPrevClose( floor0(s.getPrevClose()) );
                s.setDiff( floor0(s.getDiff()) );
            }
            else if (isUS) {
                s.setPrice( floor2(s.getPrice()) );
                s.setPrevClose( floor2(s.getPrevClose()) );
                s.setDiff( floor2(s.getDiff()) );
            }
        }

        return list;
    }

    private Double floor0(Double v) {
        if (v == null) return null;
        return Math.floor(v);
    }

    private Double floor2(Double v) {
        if (v == null) return null;
        return Math.floor(v * 100) / 100.0;
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

    public List<StrategyDetailDTO> getDetail(String strategy, LocalDate date) {
        return detailRepository
                .findByActionAndSignalDateOrderBySpecialValueAsc(strategy, date)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
