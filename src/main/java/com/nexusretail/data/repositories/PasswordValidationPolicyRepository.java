package com.nexusretail.data.repositories;

import com.nexusretail.data.models.PasswordValidationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordValidationPolicyRepository extends JpaRepository<PasswordValidationPolicy, Long> {

    boolean existsByKey(String key);
}
