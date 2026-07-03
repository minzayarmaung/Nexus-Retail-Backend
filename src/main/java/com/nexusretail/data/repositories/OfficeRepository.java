package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    @Query("select o from Office o left join fetch o.children where o.id = :id")
    Optional<Office> findByIdWithChildren(@Param("id") Long id);

    Optional<Office> findByName(String name);
}