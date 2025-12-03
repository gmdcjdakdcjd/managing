package com.stock.managing.service;

import com.stock.managing.domain.view.VCompanyInfo;
import com.stock.managing.domain.view.VCompanyInfoUs;
import com.stock.managing.repository.view.VCompanyInfoRepository;
import com.stock.managing.repository.view.VCompanyInfoUsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AutocompleteService {

    private final VCompanyInfoRepository korRepo;
    private final VCompanyInfoUsRepository usRepo;

    public List<Map<String, String>> search(String keyword) {

        if (keyword == null || keyword.isBlank()) return List.of();

        List<VCompanyInfo> kor = korRepo
                .findTop10ByNameContainingOrCodeContaining(keyword, keyword);
        List<VCompanyInfoUs> us = usRepo
                .findTop10ByNameContainingOrCodeContaining(keyword, keyword);

        List<Map<String, String>> result = new ArrayList<>();

        kor.forEach(c -> result.add(Map.of(
                "code", c.getCode(),
                "name", c.getName(),
                "market", "KOSPI"
        )));

        us.forEach(c -> result.add(Map.of(
                "code", c.getCode(),
                "name", c.getName(),
                "market", "NASDAQ"
        )));

        return result;
    }
}
