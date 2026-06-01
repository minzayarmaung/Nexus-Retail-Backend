package com.nexusretail.system.audit.service;

import com.nexusretail.common.audit.AuditSearchCriteria;
import com.nexusretail.common.dto.AuditLogDto;
import com.nexusretail.data.models.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuditService {
    Page<AuditLogDto> searchAuditLogs(AuditSearchCriteria criteria, Pageable pageable);

    AuditLogDto findById(Long id);
}
