package com.stock.managing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.stock.managing.dto.MyStockDTO;
import com.stock.managing.service.MyStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mystock")
@RequiredArgsConstructor
public class MyStockController {

    private final MyStockService myStockService;

    /**
     * 관심종목 등록 (여러 개)
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addMyStock(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestBody List<MyStockDTO> list) {

        String userId = user.getUsername();

        myStockService.addBatch(userId, list);

        return Map.of("result", "SUCCESS");
    }

    /**
     * 관심종목 목록 조회
     * (추후 UI 만들면 필요)
     */
    @GetMapping("/list")
    public String myStockList(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            Model model) {

        String userId = user.getUsername();

        List<MyStockDTO> list = myStockService.getMyStockList(userId);

        List<MyStockDTO> krList = new ArrayList<>();
        List<MyStockDTO> usList = new ArrayList<>();

        for (MyStockDTO s : list) {

            String sn = s.getStrategyName();
            if (sn == null) continue;

            if (sn.endsWith("_KR")) {
                krList.add(s);
            } else if (sn.endsWith("_US")) {
                usList.add(s);
            }
        }

        model.addAttribute("krList", krList);
        model.addAttribute("usList", usList);

        return "mystock/list";
    }



    /**
     * 관심종목 단일 삭제 (소프트 삭제)
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Map<String,Object> deleteMyStock(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        myStockService.delete(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

    /**
     * 관심종목 복구
     */
    @GetMapping("/deleted")
    @ResponseBody
    public List<MyStockDTO> deletedList(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        return myStockService.getDeletedList(user.getUsername());
    }

    @PostMapping("/restore/{id}")
    @ResponseBody
    public Map<String, Object> restore(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        myStockService.restore(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

}
