package com.nexusretail.data.repositories;

import com.nexusretail.data.models.PasswordValidationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordPreferencesRepository extends JpaRepository<PasswordValidationPolicy, Long> {

    @Modifying
    @Query("UPDATE PasswordValidationPolicy p SET p.active = false WHERE p.id <> :id")
    void deactivateOtherPolicies(@Param("id") Long id);
}
