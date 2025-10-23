package com.stock.managing.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.stock.managing.domain.Board;
import com.stock.managing.domain.QBoard;
import com.stock.managing.domain.QReply;
import com.stock.managing.dto.BoardImageDTO;
import com.stock.managing.dto.BoardListAllDTO;
import com.stock.managing.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements
        BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        query.where(board.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null && types.length > 0) && keyword != null) { //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for (String type : types) {

                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String
            keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> query = from(board);

        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if ((types != null && types.length > 0) && keyword != null) {

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for (String type : types) {

                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }

        //bno > 0
        query.where(board.bno.gt(0L));


        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
                bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ));


        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, count);
    }

//    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword,
//                                                      Pageable pageable) {
//
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //left join
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging
//
//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("-----------------");
//        });
//
//
//        return null;
//    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types,
                                               String keyword,
                                               Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //left join

        if ((types != null && types.length > 0) && keyword != null) {

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            boardJPQLQuery.where(booleanBuilder);

        }

        boardJPQLQuery.groupBy(board);


        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.
                countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);
            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();
            //BoardImage를 BoardImageDTO 처리할 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }


}