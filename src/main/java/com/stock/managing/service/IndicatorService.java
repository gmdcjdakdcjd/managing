package com.stock.managing.service;

import com.stock.managing.domain.DailyPriceIndicator;
import com.stock.managing.dto.MarketIndicatorDTO;
import com.stock.managing.repository.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final IndicatorRepository repository;

    public List<MarketIndicatorDTO> getIndicator(String code) {
        return repository.findByCodeOrderByDateAsc(code)
                .stream()
                .map(m -> MarketIndicatorDTO.builder()
                        .date(m.getDate())
                        .close(m.getClose())
                        .changeAmount(m.getChangeAmount())
                        .changeRate(m.getChangeRate())
                        .build()
                )
                .toList();
    }
}