package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRespository extends JpaRepository<Holiday, Long> {

    @Query("""
        SELECT DISTINCT h
        FROM Holiday h
        LEFT JOIN h.offices o
        WHERE
            (:officeId IS NULL OR o.id = :officeId)
        AND
            (:fromDate IS NULL OR h.toDate >= :fromDate)
        AND
            (:toDate IS NULL OR h.fromDate <= :toDate)
    """)
    List<Holiday> findAllBySearchParameters(
            @Param("officeId") Long officeId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
