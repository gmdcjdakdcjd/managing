package com.stock.managing.service;

import com.stock.managing.dto.BoardDTO;
import com.stock.managing.dto.PageRequestDTO;
import com.stock.managing.dto.PageResponseDTO;
import org.springframework.data.domain.Page;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO bordDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
