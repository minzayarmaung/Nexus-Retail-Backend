package com.nexusretail.data.repositories;

import com.nexusretail.data.models.PasswordValidationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordPreferencesRepository extends JpaRepository<PasswordValidationPolicy, Long> {

}
