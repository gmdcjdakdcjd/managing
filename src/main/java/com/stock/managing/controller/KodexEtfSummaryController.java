package com.stock.managing.controller;

import com.stock.managing.dto.KodexEtfHoldingsDto;
import com.stock.managing.dto.KodexEtfSummaryDto;
import com.stock.managing.service.KodexEtfHoldingsService;
import com.stock.managing.service.KodexEtfSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kodex")
public class KodexEtfSummaryController {

    private final KodexEtfSummaryService kodexEtfSummaryService;
    private final KodexEtfHoldingsService kodexEtfHoldingsService;

    /**
     * KODEX ETF 요약 목록 화면
     */
    @GetMapping("/summary")
    public String summaryList(
            @RequestParam(required = false) String q,
            Model model
    ) {
        List<KodexEtfSummaryDto> list =
                (q == null || q.isBlank())
                        ? kodexEtfSummaryService.getAllSummaryList()
                        : kodexEtfSummaryService.search(q);

        model.addAttribute("list", list);
        model.addAttribute("q", q);

        return "kodex/summary";
    }


    /**
     * ETF 구성 종목 (AJAX)
     */
    @GetMapping("/holdings")
    @ResponseBody
    public List<KodexEtfHoldingsDto> holdings(@RequestParam String etfId) {
        return kodexEtfHoldingsService.getHoldingsByEtfId(etfId);
    }
}
