package com.stock.managing.service;

import com.stock.managing.dto.PageRequestDTO;
import com.stock.managing.dto.PageResponseDTO;
import com.stock.managing.dto.ReplyDTO;


public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno,
                                             PageRequestDTO pageRequestDTO);
}

