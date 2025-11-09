package com.stock.managing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.managing.dto.BoardDTO;
import com.stock.managing.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Log4j2
public class BoardRestController {

    private final BoardService boardService;

    /**
     * ✅ 외부 배치서버(stock_batch_scheduler)에서 자동 게시글 등록 요청을 받는 엔드포인트
     * Content-Type: application/json
     */
    @PostMapping("/auto")
    public ResponseEntity<String> registerAuto(@RequestBody BoardDTO boardDTO) {
        log.info("📩 [자동등록 요청 수신] " + boardDTO);

        try {
            Long bno = boardService.register(boardDTO);
            log.info("✅ 게시글 등록 완료 (bno={})", bno);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("❌ 자동등록 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("FAIL: " + e.getMessage());
        }
    }

    @PostMapping("/auto-with-file")
    public ResponseEntity<String> registerAutoWithFile(
            @RequestPart("board") String boardJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        log.info("📩 [자동등록+첨부파일 요청 수신]");
        try {
            // 1️⃣ JSON 문자열 → DTO 변환
            BoardDTO boardDTO = new ObjectMapper().readValue(boardJson, BoardDTO.class);
            Long bno = boardService.register(boardDTO);
            log.info("✅ 게시글 등록 완료 (bno={})", bno);

            // 2️⃣ 파일이 존재한다면 저장
            if (file != null && !file.isEmpty()) {
                boardService.saveFile(bno, file);
                log.info("📎 첨부파일 저장 완료: {}", file.getOriginalFilename());
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("❌ 자동등록+첨부파일 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("FAIL: " + e.getMessage());
        }
    }


}
