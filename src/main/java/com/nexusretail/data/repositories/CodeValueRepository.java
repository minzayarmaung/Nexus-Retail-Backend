package com.nexusretail.data.repositories;

import com.nexusretail.data.models.CodeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeValueRepository extends JpaRepository<CodeValue, Long> {

    Optional<CodeValue> findByCodeIdAndValue(Long codeId, String value);

    List<CodeValue> findByCodeIdOrderByOrderPositionAsc(Long codeId);

    boolean existsByCodeIdAndValue(Long codeId, String value);
}
