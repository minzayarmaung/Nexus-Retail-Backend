package com.nexusretail.data.repositories;

import com.nexusretail.data.models.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByCodeType(String codeType);
}
