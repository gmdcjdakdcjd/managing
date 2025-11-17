package com.stock.managing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stock.managing.dto.*;
import com.stock.managing.service.BoardService;
import com.stock.managing.service.IndicatorService;
import com.stock.managing.service.StockViewService;
import org.springframework.beans.factory.annotation.Value;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stock.managing.service.MarkdownService; // added
import org.springframework.beans.factory.annotation.Value; // keep single import


@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MarkdownService markdownService; // added
    private final StockViewService stockService;
    private final IndicatorService indicatorService;


    @Value("${com.stock.upload.path}")
    private String uploadPath;

    @GetMapping("/index")
    public String index(Model model) {

        var kospiList = indicatorService.getIndicator("KOSPI");
        var spxList = indicatorService.getIndicator("SPX");
        var usdList = indicatorService.getIndicator("USD");
        var jpyList = indicatorService.getIndicator("USDJPY");
        var goldKrList = indicatorService.getIndicator("GOLD_KR");
        var goldGlobalList = indicatorService.getIndicator("GOLD_GLOBAL");
        var wtiList = indicatorService.getIndicator("WTI");
        var dubaiList = indicatorService.getIndicator("DUBAI");

        log.info("KOSPI   → {}", kospiList);
        log.info("SPX     → {}", spxList);
        log.info("USD     → {}", usdList);
        log.info("JPY     → {}", jpyList);
        log.info("GOLD_KR → {}", goldKrList);
        log.info("GOLD_GL → {}", goldGlobalList);
        log.info("WTI     → {}", wtiList);
        log.info("DUBAI   → {}", dubaiList);

        model.addAttribute("kospiList", convert(kospiList));
        model.addAttribute("spxList", convert(spxList));
        model.addAttribute("usdList", convert(usdList));
        model.addAttribute("jpyList", convert(jpyList));
        model.addAttribute("goldKrList", convert(goldKrList));
        model.addAttribute("goldGlobalList", convert(goldGlobalList));
        model.addAttribute("wtiList", convert(wtiList));
        model.addAttribute("dubaiList", convert(dubaiList));

        convert(kospiList).forEach(m -> log.info("Converted → date={}, close={}", m.get("date"), m.get("close")));


        return "board/index";
    }


    private List<Map<String, Object>> convert(List<MarketIndicatorDTO> list) {
        return list.stream()
                .map(dto -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("date", dto.getDate().toString());
                    m.put("close", dto.getClose());
                    return m;
                })
                .toList();
    }


    // 한국 주식
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }
    // 미국 주식
    @GetMapping("/listUS")
    public void listUS(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAllUS(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/issue")
    public String issue(Model model) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // ✅ 각 전략별 데이터 조회
        List<BoardDTO> etfList = boardService.getLatestOrTodayBoard("4", today);    // ETF 거래량
        List<BoardDTO> stockList = boardService.getLatestOrTodayBoard("3", today);  // 종목 거래량
        List<BoardDTO> riseList = boardService.getLatestOrTodayBoard("1", today);   // 급등
        List<BoardDTO> dropList = boardService.getLatestOrTodayBoard("2", today);   // 급락
        List<BoardDTO> etfList_US = boardService.getLatestOrTodayBoard("44", today);    // 미국 ETF 거래량
        List<BoardDTO> stockList_US = boardService.getLatestOrTodayBoard("43", today);  // 미국 종목 거래량
        List<BoardDTO> riseList_US = boardService.getLatestOrTodayBoard("41", today);   // 미국 급등
        List<BoardDTO> dropList_US = boardService.getLatestOrTodayBoard("42", today);   // 미국 급락

        // ✅ 개별 fallback 처리
        if (etfList.isEmpty()) {
            log.info("📅 ETF 데이터 없음 → 어제 데이터로 대체");
            etfList = boardService.getTodayBoard("4", yesterday);
        }

        if (stockList.isEmpty()) {
            log.info("📅 종목 거래량 데이터 없음 → 어제 데이터로 대체");
            stockList = boardService.getTodayBoard("3", yesterday);
        }

        if (riseList.isEmpty()) {
            log.info("📅 급등 데이터 없음 → 어제 데이터로 대체");
            riseList = boardService.getTodayBoard("1", yesterday);
        }

        if (dropList.isEmpty()) {
            log.info("📅 급락 데이터 없음 → 어제 데이터로 대체");
            dropList = boardService.getTodayBoard("2", yesterday);
        }

        //--------------------------------//
        if (etfList_US.isEmpty()) {
            log.info("📅 ETF 데이터 없음 → 어제 데이터로 대체");
            etfList_US = boardService.getTodayBoard("44", yesterday);
        }

        if (stockList_US.isEmpty()) {
            log.info("📅 종목 거래량 데이터 없음 → 어제 데이터로 대체");
            stockList_US = boardService.getTodayBoard("43", yesterday);
        }

        if (riseList_US.isEmpty()) {
            log.info("📅 급등 데이터 없음 → 어제 데이터로 대체");
            riseList_US = boardService.getTodayBoard("41", yesterday);
        }

        if (dropList_US.isEmpty()) {
            log.info("📅 급락 데이터 없음 → 어제 데이터로 대체");
            dropList_US = boardService.getTodayBoard("42", yesterday);
        }

        // ✅ 모델 등록
        model.addAttribute("etfList", etfList);
        model.addAttribute("stockList", stockList);
        model.addAttribute("riseList", riseList);
        model.addAttribute("dropList", dropList);

        model.addAttribute("etfList_US", etfList_US);
        model.addAttribute("stockList_US", stockList_US);
        model.addAttribute("riseList_US", riseList_US);
        model.addAttribute("dropList_US", dropList_US);

        log.info("🏠 Home Model Data: {}", model.asMap());

        return "board/issue"; // templates/board/issue.html 렌더링
    }

    @GetMapping("/dualMomentumList")
    public String dualMomentumList(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        model.addAttribute("monthList", boardService.getLatestOrTodayBoard("6", today));
        model.addAttribute("quarterList", boardService.getLatestOrTodayBoard("7", today));
        model.addAttribute("halfList", boardService.getLatestOrTodayBoard("8", today));
        model.addAttribute("yearList", boardService.getLatestOrTodayBoard("9", today));

        model.addAttribute("monthList_US", boardService.getLatestOrTodayBoard("46", today));
        model.addAttribute("quarterList_US", boardService.getLatestOrTodayBoard("47", today));
        model.addAttribute("halfList_US", boardService.getLatestOrTodayBoard("48", today));
        model.addAttribute("yearList_US", boardService.getLatestOrTodayBoard("49", today));

        return "board/dualMomentum";
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        List<BoardDTO> reports = boardService.getAdminReports().stream()
                .peek(dto -> dto.setContent(markdownService.toHtml(dto.getContent()))) // ✅ Markdown → HTML
                .toList();

        model.addAttribute("reports", reports);
        return "board/adminDashboard";
    }

    @GetMapping("/stockInfo")
    public String stockInfo() {
        return "board/stockInfo";
    }

    @GetMapping("/searchStock")
    public String searchStock(
            @RequestParam(required = false) String stockName,
            @RequestParam(required = false) String stockCode,
            Model model) {

        log.info("🔍 종목 검색 요청: stockName={}, stockCode={}", stockName, stockCode);

        if ((stockName == null || stockName.isBlank()) && (stockCode == null || stockCode.isBlank())) {
            model.addAttribute("error", "종목명 또는 종목코드를 입력해주세요.");
            return "board/stockInfo";
        }

        String keyword = (stockName != null && !stockName.isBlank()) ? stockName : stockCode;

        List<SignalInfoDTO> signalList = boardService.getSignalInfoListByKeyword(keyword);
        model.addAttribute("signalList", signalList);

        StockDTO stockInfo = stockService.getStockInfo(stockName, stockCode);
        log.info("✅ stockInfo 결과: {}", stockInfo);

        if (stockInfo == null) {
            model.addAttribute("stock", null);
            model.addAttribute("error", "해당 종목 정보를 찾을 수 없습니다.");
            model.addAttribute("priceList", Collections.emptyList());
        } else {
            model.addAttribute("stock", stockInfo);

            // ✅ LocalDate → String 변환 처리
            List<Map<String, Object>> safeList = stockInfo.getPriceList().stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", p.getDate() != null ? p.getDate().toString() : null);
                        map.put("open", p.getOpen());
                        map.put("high", p.getHigh());
                        map.put("low", p.getLow());
                        map.put("close", p.getClose());
                        map.put("volume", p.getVolume());
                        return map;
                    })
                    .toList();


            model.addAttribute("priceList", safeList);
        }

        return "board/stockInfo";
    }



    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/register")
    public void registerGET() { }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("board POST register.......");

        if (bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    // 로그인 안 해도 접근 가능 (게시글 읽기)
    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        String contentHtml = markdownService.toHtml(boardDTO.getContent());

        model.addAttribute("dto", boardDTO);
        model.addAttribute("contentHtml", contentHtml);
    }

    // 로그인해야 접근 가능 (게시글 수정)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public void modify(Long bno, PageRequestDTO pageRequestDTO, Model model) {

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) {

        log.info("board modify post......." + boardDTO);

        if (bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        log.info("remove post.. " + bno);

        boardService.remove(bno);

        //게시물이 데이터베이스 상에서 삭제되었다면 첨부파일 삭제
        log.info(boardDTO.getFileNames());

        List<String> fileNames = boardDTO.getFileNames();

        if (fileNames != null && !fileNames.isEmpty()) {
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

    public void removeFiles(List<String> files) {

        for (String fileName : files) {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                boolean deleted = resource.getFile().delete();
                if (!deleted) {
                    log.warn("Failed to delete file: {}", resource.getFilename());
                }
                //섬네일이 존재한다면
                if (contentType != null && contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    boolean thumbDeleted = thumbnailFile.delete();
                    if (!thumbDeleted) {
                        log.warn("Failed to delete thumbnail: {}", thumbnailFile.getName());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }//end for
    }
}