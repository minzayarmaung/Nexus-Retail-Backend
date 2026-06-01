package com.nexusretail.common.audit;

import com.nexusretail.common.dto.AuditLogDto;
import com.nexusretail.common.exception.ResourceNotFoundException;
import com.nexusretail.data.models.AuditLog;
import com.nexusretail.data.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditReadService {

    private final AuditLogRepository repository;

    public Page<AuditLogDto> findAll(AuditSearchCriteria criteria, Pageable pageable) {
        return repository.findAll(buildSpec(criteria), pageable).map(AuditLogDto::from);
    }

    public AuditLogDto findById(Long id) {
        return repository.findById(id).map(AuditLogDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", id));
    }

    private Specification<AuditLog> buildSpec(AuditSearchCriteria c) {
        return Specification
                .where(eq("actionName",        c.getAction()))
                .and(eq("entityName",          c.getEntityName()))
                .and(eq("entityId",            c.getEntityId()))
                .and(like("makerUsername",     c.getMakerUsername()))
                .and(eq("processingResult",    c.getProcessingResult()))
                .and(between("madeOnDate",     c.getFrom(), c.getTo()));
    }

    // Generic spec helpers — add new filters here without touching existing logic (OCP)
    private <T> Specification<AuditLog> eq(String field, T val) {
        return (r, q, cb) -> val == null ? null : cb.equal(r.get(field), val);
    }

    private Specification<AuditLog> like(String field, String val) {
        return (r, q, cb) -> val == null ? null :
                cb.like(cb.lower(r.get(field)), "%" + val.toLowerCase() + "%");
    }

    private Specification<AuditLog> between(String field, Instant from, Instant to) {
        return (r, q, cb) -> {
            if (from == null && to == null) return null;
            if (from == null) return cb.lessThanOrEqualTo(r.get(field), to);
            if (to == null)   return cb.greaterThanOrEqualTo(r.get(field), from);
            return cb.between(r.get(field), from, to);
        };
    }
}