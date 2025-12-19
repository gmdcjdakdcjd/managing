package com.stock.managing.repository;


import com.stock.managing.domain.KodexEtfSummary;
import com.stock.managing.domain.KodexEtfSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface KodexEtfSummaryRepository
        extends JpaRepository<KodexEtfSummary, KodexEtfSummaryId> {

    List<KodexEtfSummary>
    findByEtfNameContainingIgnoreCaseOrId_EtfIdContainingIgnoreCase(
            String etfName,
            String etfId
    );
}
