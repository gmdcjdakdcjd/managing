package com.stock.managing.repository;

import com.stock.managing.domain.KodexEtfSummary;
import com.stock.managing.domain.KodexEtfSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KodexEtfSummaryRepository
        extends JpaRepository<KodexEtfSummary, KodexEtfSummaryId> {

    // ETF명 / ETF코드 검색
    List<KodexEtfSummary>
    findByEtfNameContainingIgnoreCaseOrId_EtfIdContainingIgnoreCase(
            String etfName,
            String etfId
    );

    // 종목 검색 → ETF ID 목록으로 summary 조회 (추가)
    List<KodexEtfSummary>
    findById_EtfIdIn(List<String> etfIds);
}
