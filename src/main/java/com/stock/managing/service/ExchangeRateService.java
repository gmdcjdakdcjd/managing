package com.stock.managing.service;

import com.stock.managing.repository.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final IndicatorRepository indicatorRepository;

    public double getUsdRate() {
        Double rate = indicatorRepository.findLatestValue("usd");

        if (rate == null) {
            throw new IllegalStateException("USD 환율 정보 없음");
        }
        return rate;
    }
}
