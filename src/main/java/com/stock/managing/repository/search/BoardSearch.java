package com.stock.managing.repository.search;

import com.stock.managing.domain.Board;
import com.stock.managing.dto.BoardListAllDTO;
import com.stock.managing.dto.BoardListReplyCountDTO;
import com.stock.managing.dto.PageRequestDTO;
import com.stock.managing.dto.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, String regDate, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, String regDate, Pageable pageable);

//    Page<BoardListReplyCountDTO> searchWithAll(String[] types,
//                                               String keyword,
//                                               Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, String regDate, Pageable pageable);
    Page<BoardListAllDTO> searchAllUS(String[] types, String keyword, String regDate, Pageable pageable);

}
