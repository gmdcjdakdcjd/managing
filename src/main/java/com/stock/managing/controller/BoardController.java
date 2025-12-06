package com.stock.managing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stock.managing.domain.BondDailyYield;
import com.stock.managing.dto.*;
import com.stock.managing.enums.KRStrategy;
import com.stock.managing.enums.StrategyCode;
import com.stock.managing.enums.USStrategy;
import com.stock.managing.service.*;
import org.springframework.beans.factory.annotation.Value;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    private final BondService bondService;  // ← 이거 추가
    private final StrategyDetailService strategyDetailService;
    private final StrategyResultService strategyResultService;
    private final AutocompleteService autocompleteService;

    @Value("${com.stock.upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String rootToIndex() {
        return "board/index";
    }

    @GetMapping("/indicator")
    public String indicator(Model model) {

        var kospiList = indicatorService.getIndicator("KOSPI");
        var spxList = indicatorService.getIndicator("SNP500");
        var usdList = indicatorService.getIndicator("USD");
        var jpyList = indicatorService.getIndicator("USD_JPY");
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


        return "board/indicator";
    }

    @GetMapping("/bond")
    public String bond(Model model) {

        // ------------------------------
        // 미국 금리 (2Y, 5Y, 10Y, 30Y)
        // ------------------------------
        var us2y = bondService.getBondYield("US2YT=X");
        var us5y = bondService.getBondYield("US5YT=X");
        var us10y = bondService.getBondYield("US10YT=X");
        var us30y = bondService.getBondYield("US30YT=X");

        // ------------------------------
        // 한국 금리 (3Y, 5Y, 10Y, 30Y)
        // ------------------------------
        var kr3y = bondService.getBondYield("KR3YT=RR");
        var kr5y = bondService.getBondYield("KR5YT=RR");
        var kr10y = bondService.getBondYield("KR10YT=RR");
        var kr20y = bondService.getBondYield("KR20YT=RR");

        // ------------------------------
        // 일본 금리 (2Y, 5Y, 10Y, 30Y)
        // ------------------------------
        var jp2y = bondService.getBondYield("JP2YT=XX");
        var jp5y = bondService.getBondYield("JP5YT=XX");
        var jp10y = bondService.getBondYield("JP10YT=XX");
        var jp30y = bondService.getBondYield("JP30YT=XX");

        // ------------------------------
        // 중국 금리 (1Y, 3Y, 5Y, 10Y)
        // ------------------------------
        var cn1y = bondService.getBondYield("CN1YT=RR");
        var cn3y = bondService.getBondYield("CN3YT=RR");
        var cn5y = bondService.getBondYield("CN5YT=RR");
        var cn10y = bondService.getBondYield("CN10YT=RR");

        // ------------------------------
        // Thymeleaf에 전달
        // ------------------------------
        model.addAttribute("us2y", convertBond(us2y));
        model.addAttribute("us5y", convertBond(us5y));
        model.addAttribute("us10y", convertBond(us10y));
        model.addAttribute("us30y", convertBond(us30y));

        model.addAttribute("kr3y", convertBond(kr3y));
        model.addAttribute("kr5y", convertBond(kr5y));
        model.addAttribute("kr10y", convertBond(kr10y));
        model.addAttribute("kr20y", convertBond(kr20y));

        model.addAttribute("jp2y", convertBond(jp2y));
        model.addAttribute("jp5y", convertBond(jp5y));
        model.addAttribute("jp10y", convertBond(jp10y));
        model.addAttribute("jp30y", convertBond(jp30y));

        model.addAttribute("cn1y", convertBond(cn1y));
        model.addAttribute("cn3y", convertBond(cn3y));
        model.addAttribute("cn5y", convertBond(cn5y));
        model.addAttribute("cn10y", convertBond(cn10y));

        log.info("US 10Y → {}", us10y);
        log.info("KR 10Y → {}", kr10y);

        return "board/bond";
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

    private List<Map<String, Object>> convertBond(List<BondDailyYieldDTO> list) {
        return list.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", dto.getDate().toString());
                    map.put("close", dto.getClose());
                    map.put("open", dto.getOpen());
                    map.put("high", dto.getHigh());
                    map.put("low", dto.getLow());
                    map.put("diff", dto.getDiff());
                    return map;
                })
                .toList();
    }


    // 한국 주식
    @GetMapping("/listKR")
    public String listKR(
            PageRequestDTO pageRequestDTO,
            @RequestParam(value = "strategy", required = false) String strategy,
            @RequestParam(value = "regDate", required = false) LocalDate regDate,
            Model model) {

       /* PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);*/


        // 📌 전략명 리스트 — 고정된 KR 전략만
        //List<String> strategyList = strategyResultService.getKRStrategyList();
        // model.addAttribute("strategyList", strategyList);

        model.addAttribute("strategyList",
                Arrays.stream(StrategyCode.values())
                        .filter(s -> s.getMarket().equals("KR"))
                        .toList());



        // 📌 필터링된 결과 조회
        PageResponseDTO<StrategyResultDTO> responseDTO =
                strategyResultService.listKR(pageRequestDTO, strategy, regDate);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("strategy", strategy);
        model.addAttribute("regDate", regDate);

        return "board/resultListKR";
    }

    @GetMapping("/detailKR")
    public String detailKR(
            @RequestParam("strategy") String strategy,
            @RequestParam("date") LocalDate date,
            Model model) {

        List<StrategyDetailDTO> list = strategyDetailService.getDetail(strategy, date);

        // 전략명 Enum 매핑
        KRStrategy KRstrategyEnum = KRStrategy.from(strategy);
        String captureName = (KRstrategyEnum != null) ? KRstrategyEnum.getCaptureName() : "포착값";

        // 종가 헤더명
        String priceLabel = strategy.contains("WEEKLY") ? "전주종가" : "전일종가";

        model.addAttribute("priceLabel", priceLabel);
        model.addAttribute("captureName", captureName);
        model.addAttribute("strategy", strategy);
        model.addAttribute("date", date);
        model.addAttribute("detailList", list);

        return "board/detailKR";
    }

    // 미국 주식
    @GetMapping("/listUS")
    public String listUS(
                         PageRequestDTO pageRequestDTO,
                         @RequestParam(value = "strategy", required = false) String strategy,
                         @RequestParam(value = "regDate", required = false) LocalDate regDate,
                         Model model) {

/*        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAllUS(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);*/

        // 📌 전략명 리스트 — 고정된 US 전략만
        // List<String> strategyList = strategyResultService.getUSStrategyList();
        // model.addAttribute("strategyList", strategyList);
        model.addAttribute("strategyList",
                Arrays.stream(StrategyCode.values())
                        .filter(s -> s.getMarket().equals("US"))
                        .toList());

        // 📌 필터링된 결과 조회
        PageResponseDTO<StrategyResultDTO> responseDTO =
                strategyResultService.listUS(pageRequestDTO, strategy, regDate);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("strategy", strategy);
        model.addAttribute("regDate", regDate);


        return "board/resultListUS";
    }

    @GetMapping("/detailUS")
    public String detailUS(
            @RequestParam("strategy") String strategy,
            @RequestParam("date") LocalDate date,
            Model model) {

        List<StrategyDetailDTO> list = strategyDetailService.getDetail(strategy, date);

        // 🔥 미국: 소수점 2자리까지 버림
        List<StrategyDetailDTO> normalized = list.stream()
                .map(dto -> {
                    dto.setPrice(Math.floor(dto.getPrice() * 100) / 100.0);
                    dto.setPrevClose(Math.floor(dto.getPrevClose() * 100) / 100.0);
                    dto.setDiff(Math.floor(dto.getDiff() * 100) / 100.0);
                    return dto;
                })
                .toList();

        // 전략명 Enum 매핑
        USStrategy USstrategyEnum = USStrategy.from(strategy);
        String captureName = (USstrategyEnum != null) ? USstrategyEnum.getCaptureName() : "포착값";

        String priceLabel = strategy.contains("WEEKLY") ? "전주종가" : "전일종가";

        model.addAttribute("priceLabel", priceLabel);
        model.addAttribute("captureName", captureName);
        model.addAttribute("strategy", strategy);
        model.addAttribute("date", date);
        model.addAttribute("detailList", normalized);

        return "board/detailUS";
    }


    @GetMapping("/issue")
    public String issue(Model model) {

        String[] strategies = {
                "DAILY_DROP_SPIKE_KR",
                "DAILY_RISE_SPIKE_KR",
                "DAILY_TOP20_VOLUME_KR",
                "ETF_TOP20_VOLUME_KR",
                "DAILY_DROP_SPIKE_US",
                "DAILY_RISE_SPIKE_US",
                "DAILY_TOP20_VOLUME_US",
                "ETF_TOP20_VOLUME_US"
        };

        Map<String, List<StrategyDetailDTO>> data = new LinkedHashMap<>();

        String today = LocalDate.now().toString();


        for (String s : strategies) {
            List<StrategyDetailDTO> list = strategyDetailService.getLatestOrToday(s, today);
            data.put(s, list);
        }

        // -----------------------------
        // 🔥 Thymeleaf 전달
        // -----------------------------
        model.addAttribute("strategyMap", data);

        return "board/issue";

        /*LocalDate today = LocalDate.now();
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

        return "board/issue"; // templates/board/issue.html 렌더링*/
    }

    @GetMapping("/dualMomentumList")
    public String dualMomentumList(Model model) {

        String[] strategies = {
                "DUAL_MOMENTUM_1M_KR",
                "DUAL_MOMENTUM_3M_KR",
                "DUAL_MOMENTUM_6M_KR",
                "DUAL_MOMENTUM_1Y_KR",
                "DUAL_MOMENTUM_1M_US",
                "DUAL_MOMENTUM_3M_US",
                "DUAL_MOMENTUM_6M_US",
                "DUAL_MOMENTUM_1Y_US"
        };

        Map<String, List<StrategyDetailDTO>> data = new LinkedHashMap<>();

        String today = LocalDate.now().toString();

        for (String s : strategies) {
            List<StrategyDetailDTO> raw = strategyDetailService.getLatestOrToday(s, today);

            boolean isKR = s.endsWith("_KR");
            List<StrategyDetailDTO> normalized = normalizeMomentumData(raw, isKR);

            data.put(s, normalized);
        }

        model.addAttribute("strategyMap", data);
        return "board/dualMomentum";
    }


    private List<StrategyDetailDTO> normalizeMomentumData(List<StrategyDetailDTO> list, boolean isKR) {
        return list.stream().map(item -> {

            double price = item.getPrice();
            double prev = item.getPrevClose();
            double diff = item.getDiff();

            if (isKR) {
                // 한국: 정수(문자열로 보냄 → 화면에서 236500.0 방지)
                item.setPrice((double)((long)price));
                item.setPrevClose((double)((long)prev));
                item.setDiff((double)((long)diff));
            } else {
                // 미국: 소수점 2자리 버림
                item.setPrice(Math.floor(price * 100) / 100.0);
                item.setPrevClose(Math.floor(prev * 100) / 100.0);
                item.setDiff(Math.floor(diff * 100) / 100.0);
            }

            return item;
        }).toList();
    }



    @PreAuthorize("hasRole('ADMIN')")
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

        List<StrategyDetailDTO> signalList = strategyDetailService.searchDetail(keyword);
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

                        boolean isKR = "KOSPI".equals(stockInfo.getMarketType()) ||
                                "KOSDAQ".equals(stockInfo.getMarketType());

                        double open  = p.getOpen();
                        double high  = p.getHigh();
                        double low   = p.getLow();
                        double close = p.getClose();

                        if (isKR) {
                            // 한국: 정수 변환
                            map.put("open",  (long) open);
                            map.put("high",  (long) high);
                            map.put("low",   (long) low);
                            map.put("close", (long) close);
                        } else {
                            // 미국: 소수점 2 자리 버림
                            map.put("open",  Math.floor(open  * 100) / 100.0);
                            map.put("high",  Math.floor(high  * 100) / 100.0);
                            map.put("low",   Math.floor(low   * 100) / 100.0);
                            map.put("close", Math.floor(close * 100) / 100.0);
                        }

                        map.put("volume", p.getVolume());
                        return map;
                    })
                    .toList();

            model.addAttribute("priceList", safeList);

        }

        return "board/stockInfo";
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<Map<String, String>> autocomplete(@RequestParam("q") String keyword) {
        return autocompleteService.search(keyword);
    }

    @GetMapping("/autocompleteCode")
    @ResponseBody
    public List<Map<String, String>> autocompleteCode(@RequestParam("q") String keyword) {
        return autocompleteService.search(keyword);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/register")
    public void registerGET() {
    }

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