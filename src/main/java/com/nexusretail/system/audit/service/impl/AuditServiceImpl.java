package com.nexusretail.system.audit.service.impl;

import com.nexusretail.common.audit.AuditSearchCriteria;
import com.nexusretail.common.dto.AuditLogDto;
import com.nexusretail.common.exception.ResourceNotFoundException;
import com.nexusretail.data.models.AuditLog;
import com.nexusretail.data.repositories.AuditLogRepository;
import com.nexusretail.system.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository repository;

    @Override
    public Page<AuditLogDto> searchAuditLogs(AuditSearchCriteria c, Pageable pageable) {
        return repository.findAll(buildSpec(c), pageable).map(AuditLogDto::from);
    }

    @Override
    public AuditLogDto findById(Long id) {
        return repository.findById(id)
                .map(AuditLogDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", id));
    }

    private Specification<AuditLog> buildSpec(AuditSearchCriteria c) {
        return Specification
                .where(like("actionName",              c.getAction()))
                .and(like("entityName",                c.getEntityName()))
                .and(eq("entityId",                    c.getEntityId()))
                .and(like("makerName",                 c.getMakerName()))
                .and(eq("processingResult",            c.getProcessingResult()))
                .and(betweenDates(                     c.getFrom(), c.getTo()))
                .and(like("actionMethod",              c.getActionMethod()))
                .and(like("browserName",               c.getBrowserName()))
                .and(like("deviceModel",               c.getDeviceModel()))
                .and(like("operatingSystem",           c.getOperatingSystem()))
                .and(like("operatingSystemVersion",    c.getOperatingSystemVersion()));
    }

    private Specification<AuditLog> like(String field, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) return null;
            return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase().trim() + "%");
        };
    }

    private <T> Specification<AuditLog> eq(String field, T value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get(field), value);
    }

    private Specification<AuditLog> betweenDates(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            Timestamp tsFrom = from != null ? Timestamp.from(from) : null;
            Timestamp tsTo   = to   != null ? Timestamp.from(to)   : null;
            if (tsFrom == null) return cb.lessThanOrEqualTo(root.get("madeOnDate"), tsTo);
            if (tsTo   == null) return cb.greaterThanOrEqualTo(root.get("madeOnDate"), tsFrom);
            return cb.between(root.get("madeOnDate"), tsFrom, tsTo);
        };
    }
}