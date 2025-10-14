package com.stock.managing.service;

import com.stock.managing.dto.BoardDTO;
import com.stock.managing.dto.BoardListReplyCountDTO;
import com.stock.managing.dto.PageRequestDTO;
import com.stock.managing.dto.PageResponseDTO;
import org.springframework.data.domain.Page;

public interface BoardService {


    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO
                                                                       pageRequestDTO);
}
