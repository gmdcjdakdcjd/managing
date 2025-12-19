package com.stock.managing.service;

import com.stock.managing.dto.KodexEtfSummaryDto;
import java.util.List;

public interface KodexEtfSummaryService {
    List<KodexEtfSummaryDto> getAllSummaryList();
    List<KodexEtfSummaryDto> search(String keyword);
}
