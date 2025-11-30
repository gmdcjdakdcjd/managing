package com.stock.managing.service;

import com.stock.managing.domain.BondDailyYield;
import com.stock.managing.dto.BondDailyYieldDTO;
import com.stock.managing.repository.BondYieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BondService {

    private final BondYieldRepository repository;

    public List<BondDailyYieldDTO> getBondYield(String code) {

        return repository.findByCodeOrderByDateAsc(code)
                .stream()
                .map(b -> BondDailyYieldDTO.builder()
                        .code(b.getCode())
                        .date(b.getDate())
                        .open(b.getOpen())
                        .high(b.getHigh())
                        .low(b.getLow())
                        .close(b.getClose())
                        .diff(b.getDiff())
                        .lastUpdate(b.getLastUpdate())
                        .build()
                )
                .toList();
    }
}
