package com.stock.managing.service;

import com.stock.managing.domain.Board;
import com.stock.managing.dto.*;
import com.stock.managing.repository.BoardRepository;
import com.stock.managing.repository.SignalInfoRepository;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;


    @Override
    public Long register(BoardDTO boardDTO) {

        // 1️⃣ DTO → 엔티티 변환 (이미 파일 포함됨)
        Board board = dtoToEntity(boardDTO);

        // 2️⃣ 저장 (CascadeType.ALL로 이미지도 같이 저장됨)
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }


    @Override
    public BoardDTO readOne(Long bno) {

        //board_image까지 조인 처리되는 findByWithImages()를 이용
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }


    @Override
    public void modify(BoardDTO boardDTO) {

        // 1️⃣ 기존 게시글 + 이미지 세트까지 한번에 조회
        Optional<Board> result = boardRepository.findByIdWithImages(boardDTO.getBno());
        Board board = result.orElseThrow();

        // 2️⃣ 기본 필드(제목, 내용) 수정
        board.change(boardDTO.getTitle(), boardDTO.getContent(), boardDTO.getBoardGb());

        // 3️⃣ 새 파일 정보가 들어온 경우에만 기존 첨부파일 정리
        List<String> newFiles = boardDTO.getFileNames();

        if (newFiles != null && !newFiles.isEmpty()) {
            log.info("기존 이미지 초기화 및 새 이미지 추가 시작...");

            // 🔹 기존 이미지 관계 해제 및 orphanRemoval 처리
            board.clearImages();

            // 🔹 새 이미지 추가
            newFiles.forEach(fileName -> {
                String[] arr = fileName.split("_", 2); // 파일명에 '_'가 들어가도 안전하게 분리
                if (arr.length == 2) {
                    String uuid = arr[0];
                    String name = arr[1];
                    board.addImage(uuid, name);
                }
            });
        } else {
            log.info("첨부파일 변경 없음 — 기존 이미지 유지");
        }

        // 4️⃣ JPA가 변경 감지하여 자동 업데이트
        boardRepository.save(board);

        log.info("게시글 수정 완료 (bno: {})", board.getBno());
    }


    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String regDate = pageRequestDTO.getRegDate();
        Pageable pageable = pageRequestDTO.getPageable("bno");


        Page<Board> result = boardRepository.searchAll(types, keyword, regDate, pageable);

        log.info("📅 요청된 regDate = {}", regDate);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class)).
                collect(Collectors.toList());
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO
                                                                              pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String regDate = pageRequestDTO.getRegDate();

        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListReplyCountDTO> result = boardRepository.
                searchWithReplyCount(types, keyword, regDate, pageable);
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String regDate = pageRequestDTO.getRegDate();

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, regDate, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAllUS(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String regDate = pageRequestDTO.getRegDate();

        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchAllUS(types, keyword, regDate, pageable);

        List<BoardListAllDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardListAllDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }


    @Override
    public List<BoardDTO> getTodayBoard(String boardGb, LocalDate today) {
        List<String> contents = boardRepository.findTodayContentByBoardGb(
                boardGb, today, PageRequest.of(0, 1)
        );

        // ✅ Markdown Parser 옵션 (표 + 기타 확장 활성화)
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, List.of(TablesExtension.create())); // ★ 중요!

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        return contents.stream()
                .map(content -> {
                    Node document = parser.parse(content);
                    String html = renderer.render(document); // Markdown → HTML
                    return BoardDTO.builder()
                            .content(html)
                            .boardGb(boardGb)
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<BoardDTO> getLatestOrTodayBoard(String boardGb, LocalDate today) {
        List<BoardDTO> todayList = getTodayBoard(boardGb, today);

        if (todayList == null || todayList.isEmpty()) {
            // 오늘 데이터 없으면 → 최신 1건 대체
            return getLatestBoard(boardGb);
        }

        return todayList;
    }

    public List<BoardDTO> getLatestBoard(String boardGb) {
        Pageable pageable = PageRequest.of(0, 1);
        List<String> contents = boardRepository.findLatestContentByBoardGb(boardGb, pageable);

        // ✅ Markdown Parser 옵션 (표 + 기타 확장 활성화)
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, List.of(TablesExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        return contents.stream()
                .map(content -> {
                    Node document = parser.parse(content);
                    String html = renderer.render(document); // Markdown → HTML
                    return BoardDTO.builder()
                            .content(html)
                            .boardGb(boardGb)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardDTO> getAdminReports() {
        List<Board> reports = boardRepository.findByBoardGbOrderByBnoDesc("99");
        return reports.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


    @Value("${com.stock.upload.path}")
    private String uploadPath;

    public void saveFile(Long bno, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            log.warn("⚠️ 업로드된 파일이 없습니다 (bno={})", bno);
            return;
        }

        // 파일 기본 정보
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

        // 물리 저장
        file.transferTo(savePath.toFile());
        log.info("📄 파일 저장 완료: {}", savePath);

        // Board 엔티티 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. bno=" + bno));

        // BoardImage 엔티티 연결 (cascade 가능)
        board.addImage(uuid, originalName);
        boardRepository.save(board);

        log.info("✅ 파일 메타데이터 저장 완료 (bno={}, name={})", bno, originalName);
    }

    private final SignalInfoRepository signalInfoRepository;

    @Override
    public List<SignalInfoDTO> getSignalInfoListByKeyword(String keyword) {
        log.info("🔍 전략 포착 정보 전체 조회: {}", keyword);

        List<Object[]> results = signalInfoRepository.findAllRawByKeyword(keyword);

        if (results.isEmpty()) {
            log.info("⚠️ 전략 포착 정보 없음 → {}", keyword);
            return Collections.emptyList();
        }

        return results.stream().map(row -> SignalInfoDTO.builder()
                .name((String) row[0])
                .code((String) row[1])
                .strategyName((String) row[2])
                .signalDate(row[3] != null ? row[3].toString() : null)
                .resultId(row[4] != null ? ((Number) row[4]).longValue() : null)
                .build()
        ).toList();
    }

}