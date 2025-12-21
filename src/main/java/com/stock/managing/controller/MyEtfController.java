package com.stock.managing.controller;

import com.stock.managing.domain.MyEtfItemHistoryEntity;
import com.stock.managing.dto.MyEtfCreateRequestDto;
import com.stock.managing.dto.MyEtfDetailSummaryDto;
import com.stock.managing.dto.MyEtfEditRequestDto;
import com.stock.managing.dto.MyEtfRestoreRequestDto;
import com.stock.managing.repository.MyEtfHistoryRepository;
import com.stock.managing.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import com.stock.managing.service.MyEtfService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myetf")
public class MyEtfController {

    private final MyEtfService myEtfService;
    private final MyEtfHistoryRepository historyRepository;
    private final ExchangeRateService exchangeRateService;

    /**
     * 📌 내가 만든 ETF 목록
     * URL: /myetf/list
     */
    @GetMapping("/list")
    public String myEtfList(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            Model model
    ) {
        String userId = user.getUsername();

        model.addAttribute(
                "etfList",
                myEtfService.getMyEtfList(userId)
        );

        return "myetf/list";
    }

    /**
     * ETF 생성
     * POST /myetf/create
     */
    @PostMapping("/create")
    public ResponseEntity<?> createEtf(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestBody MyEtfCreateRequestDto request
    ) {
        String userId = user.getUsername();
        myEtfService.createEtf(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/detail")
    public String etfDetail(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestParam String etfName,
            Model model
    ) {
        String userId = user.getUsername();

        var itemList = myEtfService.getEtfItemList(userId, etfName);

        // 🔥 USD 환율 (DB 최신값)
        double usdRate = exchangeRateService.getUsdRate();

        for (var s : itemList) {

            boolean isUsStock =
                    s.getCode() != null && s.getCode().matches(".*[A-Za-z].*");

            // 값 없음 방어
            if (s.getPriceAtAdd() == null || s.getCurrentPrice() == null) {
                s.setPriceAtAddDisplay("-");
                s.setCurrentPriceDisplay("-");
                s.setEvaluatedAmountDisplay("-");
                continue;
            }

            double priceAtAdd = s.getPriceAtAdd();
            double currentPrice = s.getCurrentPrice();
            long quantity = s.getQuantity();

            if (isUsStock) {
                long addKrw = Math.round(priceAtAdd * usdRate);
                long currentKrw = Math.round(currentPrice * usdRate);
                long evaluated = currentKrw * quantity;

                long invested = addKrw * quantity;

                double profitRate =
                        invested > 0
                                ? ((evaluated - invested) * 100.0) / invested
                                : 0.0;

                s.setProfitRate(profitRate);
                s.setProfitRateDisplay(
                        String.format("%.2f%%", profitRate)
                );

                s.setPriceAtAddDisplay(
                        String.format("(%,.3f$) %,d", priceAtAdd, addKrw)
                );
                s.setCurrentPriceDisplay(
                        String.format("(%,.3f$) %,d", currentPrice, currentKrw)
                );
                s.setEvaluatedAmountDisplay(
                        String.format("%,d", evaluated)
                );

            } else {
                long addKrw = Math.round(priceAtAdd);
                long currentKrw = Math.round(currentPrice);
                long evaluated = currentKrw * quantity;

                long invested = addKrw * quantity;

                double profitRate =
                        invested > 0
                                ? ((evaluated - invested) * 100.0) / invested
                                : 0.0;

                s.setProfitRate(profitRate);
                s.setProfitRateDisplay(
                        String.format("%.2f%%", profitRate)
                );

                s.setPriceAtAddDisplay(String.format("%,d", addKrw));
                s.setCurrentPriceDisplay(String.format("%,d", currentKrw));
                s.setEvaluatedAmountDisplay(String.format("%,d", evaluated));
            }
        }


        model.addAttribute("etfName", etfName);
        model.addAttribute(
                "etfDescription",
                myEtfService.getEtfDescription(userId, etfName)
        );
        MyEtfDetailSummaryDto summary =
                myEtfService.getEtfDetailSummary(userId, etfName);

        model.addAttribute("summary", summary);
        model.addAttribute("itemList", itemList);

        return "myetf/my-etf-detail";
    }





    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfEditRequestDto request
    ) {
        String userId = user.getUsername();
        myEtfService.editEtf(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    @ResponseBody
    public List<MyEtfItemHistoryEntity> getRestoreHistory(
            @AuthenticationPrincipal User user,
            @RequestParam String etfName
    ) {
        return historyRepository
                .findByUserIdAndEtfNameAndRestoredYnOrderByDeletedAtDesc(
                        user.getUsername(),
                        etfName,
                        "N"   // ✅ 복구 안 된 것만
                );
    }


    @PostMapping("/restore")
    @ResponseBody
    public ResponseEntity<?> restoreEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfRestoreRequestDto request
    ) {
        myEtfService.restoreEtfItems(user.getUsername(), request);
        return ResponseEntity.ok().build();
    }






}
