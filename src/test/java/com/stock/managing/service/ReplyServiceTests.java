package com.stock.managing.service;


import com.stock.managing.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void tesetRegister() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(101L)
                .replyText("댓글 테스트")
                .replyer("replyer")
                .build();

        log.info(replyService.register(replyDTO));
    }

}
