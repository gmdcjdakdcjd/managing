package com.stock.managing.service;

import com.stock.managing.domain.Board;
import com.stock.managing.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    // 게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListAllDTO> listWithAllUS(PageRequestDTO pageRequestDTO);

    // ✅ DTO → Entity 변환
    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .boardGb(boardDTO.getBoardGb()) // ✅ 추가된 부분!
                .build();

        if (boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_", 2);
                if (arr.length == 2) {
                    board.addImage(arr[0], arr[1]);
                }
            });
        }
        return board;
    }

    // ✅ Entity → DTO 변환
    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .boardGb(board.getBoardGb()) // ✅ 추가된 부분!
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream()
                        .sorted()
                        .map(boardImage -> boardImage.getUuid() + "_" + boardImage.getFileName())
                        .collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

    List<BoardDTO> getTodayBoard(String boardGb, LocalDate today);

    List<BoardDTO> getLatestOrTodayBoard(String boardGb, LocalDate today);

    List<BoardDTO> getAdminReports();

    // ✅ PDF, 이미지 등 첨부파일 저장용 메서드
    void saveFile(Long bno, MultipartFile file) throws IOException;

    List<SignalInfoDTO> getSignalInfoListByKeyword(String keyword);

}
