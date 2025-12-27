package com.stock.managing.repository;

import com.stock.managing.domain.MyEtfItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyEtfItemRepository extends JpaRepository<MyEtfItemEntity, Long> {

    @Query("""
            select
                e.etfName,
                max(e.etfDescription),
                count(e.id),
                sum(coalesce(e.priceAtAdd, 0) * e.quantity)
            from MyEtfItemEntity e
            where e.userId = :userId
              and e.deletedYn = 'N'
            group by e.etfName
            having count(e.id) > 0
            order by min(e.createdAt) desc
            """)
    List<Object[]> findMyEtfSummary(@Param("userId") String userId);


    List<MyEtfItemEntity> findByUserIdAndEtfNameAndDeletedYn(
            String userId,
            String etfName,
            String deletedYn
    );

    Optional<MyEtfItemEntity> findFirstByUserIdAndEtfNameAndDeletedYn(
            String userId,
            String etfName,
            String deletedYn
    );

    Optional<MyEtfItemEntity> findByIdAndUserId(Long id, String userId);

    @Modifying
    @Query("""
        update MyEtfItemEntity e
           set e.deletedYn = 'Y',
               e.deletedAt = CURRENT_TIMESTAMP
         where e.userId = :userId
           and e.etfName = :etfName
           and e.deletedYn = 'N'
    """)
    void softDeleteByUserIdAndEtfName(
            @Param("userId") String userId,
            @Param("etfName") String etfName
    );

    Optional<MyEtfItemEntity>
    findByUserIdAndEtfNameAndCode(String userId, String etfName, String code);

    @Query("""
    select distinct e.etfName
    from MyEtfItemEntity e
    where e.userId = :userId
""")
    List<String> findDistinctEtfNameByUserId(@Param("userId") String userId);


    @Modifying
    @Query("""
            update MyEtfItemEntity e
               set e.etfDescription = :description
             where e.userId = :userId
               and e.etfName = :etfName
               and e.deletedYn = 'N'
            """)
    void updateEtfDescription(
            String userId,
            String etfName,
            String description
    );



}
