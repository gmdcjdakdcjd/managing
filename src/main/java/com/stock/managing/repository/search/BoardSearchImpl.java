package com.stock.managing.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.stock.managing.domain.Board;
import com.stock.managing.domain.QBoard;
import com.stock.managing.domain.QReply;
import com.stock.managing.dto.BoardImageDTO;
import com.stock.managing.dto.BoardListAllDTO;
import com.stock.managing.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    private static final List<String> EXCLUDED_GB = List.of("99","1","2","3","4","6","7","8","9");
    private static final List<String> INCLUDED_GB_KR = List.of("11","12","13","14","15","16","17","18","19","20");
    private static final List<String> INCLUDED_GB_US = List.of("31","32","33","34","35","36","37","38","39","40");

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

        return new PageImpl<>(list, pageable, count);
    }

    /** ✅ boardGb 기준 공통 검색 조건 생성 */
    private void applyFilter(QBoard board, JPQLQuery<?> query, String[] types, String keyword) {

        if ((types == null || types.length == 0) && (keyword == null || keyword.isEmpty())) {
            return;
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (types != null && types.length > 0 && types[0] != null && !types[0].isEmpty()) {
            for (String type : types) {
                switch (type) {
//                    case "a":   // 거래량
//                        builder.or(board.boardGb.eq("11"));
//                        break;
//                    case "b":   // 급등 / 급락
//                        builder.or(board.boardGb.eq("12"));
//                        break;

                    case "a":   // rsi 70 이상
                        builder.or(board.boardGb.eq("11"));
                        break;
                    case "b":   // rsi 30 이하
                        builder.or(board.boardGb.eq("12"));
                        break;
                    case "c":   // 52주 신고가
                        builder.or(board.boardGb.eq("13"));
                        break;
                    case "d":  // 52주 신저가
                        builder.or(board.boardGb.eq("14"));
                        break;
                    case "e": // 120일 신고가
                        builder.or(board.boardGb.eq("15"));
                        break;
                    case "f": // 120일 신저가
                        builder.or(board.boardGb.eq("16"));
                        break;
                    case "g": // 볼린저 밴드 상단 터치
                        builder.or(board.boardGb.eq("17"));
                        break;
                    case "h": // 볼린저 밴드 하단 터치
                        builder.or(board.boardGb.eq("18"));
                        break;
                    case "i": // 주봉 기준 60일 이동평균선 터치
                        builder.or(board.boardGb.eq("19"));
                        break;
                    case "j": // 일봉 기준 60일 이동평균선 터치
                        builder.or(board.boardGb.eq("20"));
                        break;
//                    case "k": // 20일 듀얼모멘텀
//                        builder.or(board.boardGb.eq("21"));
//                        break;
//                    case "l": // 60일 듀얼모멘텀
//                        builder.or(board.boardGb.eq("22"));
//                        break;
                }
            }
        }

        // ✅ keyword는 제목/내용으로 보조 검색 가능하도록 유지
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(board.title.containsIgnoreCase(keyword)
                    .or(board.content.containsIgnoreCase(keyword)));
        }

        query.where(builder);
    }

    /** 게시판 기본 검색 */
    @Override
    public Page<Board> searchAll(String[] types, String keyword, String regDate, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        //query.where(board.boardGb.notIn(EXCLUDED_GB));
        query.where(board.boardGb.in(INCLUDED_GB_KR));

        applyFilter(board, query, types, keyword);

        // 🔹 작성일 기준 하루치만 필터링
        if (regDate != null && !regDate.isEmpty()) {
            LocalDate date = LocalDate.parse(regDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            query.where(board.regDate.between(startOfDay, endOfDay));
        }

        query.where(board.bno.gt(0L));
        getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    /** 댓글 수 포함 검색 */
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, String regDate, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        // ✅ 관리자 배치글(99) 제외
        //query.where(board.boardGb.notIn(EXCLUDED_GB));
        query.where(board.boardGb.in(INCLUDED_GB_KR));

        applyFilter(board, query, types, keyword);

        // ✅ regDate 필터 추가
        if (regDate != null && !regDate.isEmpty()) {
            LocalDate date = LocalDate.parse(regDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            query.where(board.regDate.between(startOfDay, endOfDay));
        }

        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(
                BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


    /** 이미지 + 댓글수 포함 전체 검색 */
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, String regDate, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        // ✅ 여기 추가: 관리자 배치용(99) 제외
        //boardJPQLQuery.where(board.boardGb.notIn(EXCLUDED_GB));
        boardJPQLQuery.where(board.boardGb.in(INCLUDED_GB_KR));

        // ✅ 기존 필터 유지
        applyFilter(board, boardJPQLQuery, types, keyword);

        // ✅ 날짜 필터 유지
        if (regDate != null && !regDate.isEmpty()) {
            LocalDate date = LocalDate.parse(regDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            boardJPQLQuery.where(board.regDate.between(startOfDay, endOfDay));
        }

        boardJPQLQuery.groupBy(board);
        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            // 이미지 변환
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream()
                    .sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build())
                    .collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, totalCount);
    }



    /** 이미지 + 댓글수 포함 전체 검색 */
    @Override
    public Page<BoardListAllDTO> searchAllUS(String[] types, String keyword, String regDate, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        // ✅ 여기 추가: 관리자 배치용(99) 제외
        boardJPQLQuery.where(board.boardGb.in(INCLUDED_GB_US));

        // ✅ 기존 필터 유지
        applyFilterUS(board, boardJPQLQuery, types, keyword);

        // ✅ 날짜 필터 유지
        if (regDate != null && !regDate.isEmpty()) {
            LocalDate date = LocalDate.parse(regDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            boardJPQLQuery.where(board.regDate.between(startOfDay, endOfDay));
        }

        boardJPQLQuery.groupBy(board);
        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            // 이미지 변환
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream()
                    .sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build())
                    .collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private void applyFilterUS(QBoard board, JPQLQuery<?> query, String[] types, String keyword) {

        if ((types == null || types.length == 0) && (keyword == null || keyword.isEmpty())) {
            return;
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (types != null && types.length > 0 && types[0] != null && !types[0].isEmpty()) {
            for (String type : types) {
                switch (type) {
//                    case "a":   // 거래량
//                        builder.or(board.boardGb.eq("11"));
//                        break;
//                    case "b":   // 급등 / 급락
//                        builder.or(board.boardGb.eq("12"));
//                        break;

                    case "a":   // rsi 70 이상
                        builder.or(board.boardGb.eq("31"));
                        break;
                    case "b":   // rsi 30 이하
                        builder.or(board.boardGb.eq("32"));
                        break;
                    case "c":   // 52주 신고가
                        builder.or(board.boardGb.eq("33"));
                        break;
                    case "d":  // 52주 신저가
                        builder.or(board.boardGb.eq("34"));
                        break;
                    case "e": // 120일 신고가
                        builder.or(board.boardGb.eq("35"));
                        break;
                    case "f": // 120일 신저가
                        builder.or(board.boardGb.eq("36"));
                        break;
                    case "g": // 볼린저 밴드 상단 터치
                        builder.or(board.boardGb.eq("37"));
                        break;
                    case "h": // 볼린저 밴드 하단 터치
                        builder.or(board.boardGb.eq("38"));
                        break;
                    case "i": // 주봉 기준 60일 이동평균선 터치
                        builder.or(board.boardGb.eq("39"));
                        break;
                    case "j": // 일봉 기준 60일 이동평균선 터치
                        builder.or(board.boardGb.eq("40"));
                        break;
//                    case "k": // 20일 듀얼모멘텀
//                        builder.or(board.boardGb.eq("21"));
//                        break;
//                    case "l": // 60일 듀얼모멘텀
//                        builder.or(board.boardGb.eq("22"));
//                        break;
                }
            }
        }

        // ✅ keyword는 제목/내용으로 보조 검색 가능하도록 유지
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(board.title.containsIgnoreCase(keyword)
                    .or(board.content.containsIgnoreCase(keyword)));
        }

        query.where(builder);
    }

}
