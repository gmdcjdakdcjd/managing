package com.stock.managing.service;

import com.stock.managing.domain.KodexEtfSummary;
import com.stock.managing.dto.KodexEtfSummaryDto;

import com.stock.managing.repository.KodexEtfSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KodexEtfSummaryServiceImpl implements KodexEtfSummaryService {

    private final KodexEtfSummaryRepository summaryRepository;

    @Override
    public List<KodexEtfSummaryDto> getAllSummaryList() {
        return summaryRepository.findAll()
                .stream()
                .map(KodexEtfSummaryDto::fromEntity)
                .toList();
    }

    @Override
    public List<KodexEtfSummaryDto> search(String keyword) {
        return summaryRepository
                .findByEtfNameContainingIgnoreCaseOrId_EtfIdContainingIgnoreCase(
                        keyword, keyword
                )
                .stream()
                .map(KodexEtfSummaryDto::fromEntity)
                .toList();
    }
}
