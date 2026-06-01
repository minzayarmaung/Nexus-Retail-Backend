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
                // Basic filters
                .where(like("action_name",     c.getAction()))
                .and(like("entity_name",       c.getEntityName()))
                .and(eq("entity_id",           c.getEntityId()))
                .and(like("maker_name",        c.getMakerName()))
                .and(eq("processing_result",   c.getProcessingResult()))
                .and(betweenDates(             c.getFrom(), c.getTo()))
                // Advanced filters
                .and(like("action_method",               c.getActionMethod()))
                .and(like("browser_name",                c.getBrowserName()))
                .and(like("device_model",                c.getDeviceModel()))
                .and(like("operating_system",            c.getOperatingSystem()))
                .and(like("operating_system_version",    c.getOperatingSystemVersion()));
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

    private Specification<AuditLog> betweenDates(java.time.Instant from, java.time.Instant to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            Timestamp tsFrom = from != null ? Timestamp.from(from) : null;
            Timestamp tsTo   = to   != null ? Timestamp.from(to)   : null;
            if (tsFrom == null) return cb.lessThanOrEqualTo(root.get("made_on_date"), tsTo);
            if (tsTo   == null) return cb.greaterThanOrEqualTo(root.get("made_on_date"), tsFrom);
            return cb.between(root.get("made_on_date"), tsFrom, tsTo);
        };
    }
}