package com.stock.managing.service;


import com.stock.managing.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

/*    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTo = BoardDTO.builder().
                title("Test Title...").
                content("Test Content...").
                writer("user00").
                build();

        Long bno = boardService.register(boardDTo);
        log.info("BNO: " + bno);
    }*/

   /* @Test
    public void testModify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated Title...")
                .content("Updated Content...")
                .build();

        boardService.modify(boardDTO);
    }*/

    /*@Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().type("tcw").keyword("1").page(1).size(10).build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }*/

/*    @Test
    public void testRegisterWithImages() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Image Test Title")
                .content("Image Test Content")
                .writer("imageUser")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_image1.jpg",
                        UUID.randomUUID() + "_image2.jpg",
                        UUID.randomUUID() + "_image3.jpg")
        );


        Long bno = boardService.register(boardDTO);
        log.info("bno : " + bno);
    }*/

 /*   @Test
    public void testReadAll() {
        Long bno = 59L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }*/

/*    @Test
    public void testModify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("update 59")
                .content("update content 59")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_mod1.jpg"));


        boardService.modify(boardDTO);
    }*/

/*    @Test
    public void testRemoveAll(){
        Long bno = 59L;
        boardService.remove(bno);
    }*/

/*    @Test
    public void testListWithAll() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ": " + boardListAllDTO.getTitle());
            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }
            log.info("================================");
        });

        log.info(responseDTO);
    }*/
}
