package in.ashna.moneymantra.repository;

import in.ashna.moneymantra.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    //Finder methods
    //select * from tbl_expenses where profile_id = ?1 order by date desc
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    //select * from tbl_expenses where profile_id = ?1 order by date desc limit 5
    //find top 5 profile Ids and fetch it by date in desc order
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    //not finder method, custom JPQL query
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfile(@Param("profileId") Long profileId);

    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM ExpenseEntity e
    WHERE e.profile.id = :profileId
    AND e.date BETWEEN :startDate AND :endDate
    """)
    BigDecimal findTotalExpenseByProfileAndDateBetween(
            @Param("profileId") Long profileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    SELECT e.category.name, SUM(e.amount)
    FROM ExpenseEntity e
    WHERE e.profile.id = :profileId
    AND e.date BETWEEN :startDate AND :endDate
    GROUP BY e.category.name
    ORDER BY SUM(e.amount) DESC
""")
    List<Object[]> findExpenseTotalsByCategory(
            @Param("profileId") Long profileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3 and name like %?4%
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    //select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    //select * from tbl_expenses where profile_id = ?1 and date = ?2
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

}
