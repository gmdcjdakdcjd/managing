package com.stock.managing.repository;

import com.stock.managing.domain.Board;
import com.stock.managing.repository.search.BoardSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages(Long bno);

    @Query("SELECT b.content FROM Board b " +
            "WHERE b.boardGb = :boardGb " +
            "AND FUNCTION('DATE', b.regDate) = :today " +
            "ORDER BY b.regDate DESC")
    List<String> findTodayContentByBoardGb(@Param("boardGb") String boardGb,
                                           @Param("today") LocalDate today,
                                           Pageable pageable);

    @Query("SELECT b.content FROM Board b " +
            "WHERE b.boardGb = :boardGb " +
            "ORDER BY b.regDate DESC")
    List<String> findLatestContentByBoardGb(@Param("boardGb") String boardGb, Pageable pageable);

    List<Board> findByBoardGbOrderByBnoDesc(String boardGb);

}