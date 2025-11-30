package com.stock.managing.controller;

import com.stock.managing.dto.BatchDateGroupDTO;
import com.stock.managing.service.ManageBatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ManageController {

    private final ManageBatchHistoryService historyService;


    @GetMapping("/manage/batch/history")
    public String viewBatchHistory(Model model) {

        // ✔ Service에서 dateKey 포함된 그룹 DTO 가져오기
        List<BatchDateGroupDTO> groups = historyService.getGroupedHistory();

        model.addAttribute("groups", groups);

        return "manage/batch/history";
    }
}

