package com.nexusretail.common.audit;

import com.nexusretail.data.models.AuditLog;
import com.nexusretail.data.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventPersistenceHandler {

    private final AuditLogRepository    repository;
    private final AuditSerializerRegistry serializerRegistry;

    @Async("auditTaskExecutor")
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(AuditEvent event) {
        try {
            String payload = serializerRegistry.serialize(
                    event.commandArgs() != null && event.commandArgs().length > 0
                            ? event.commandArgs()[0]
                            : null
            );

            AuditLog auditLog = AuditLog.builder()
                    .action_name(event.action())
                    .entity_name(event.entityName())
                    .entity_id(event.entityId())
                    .maker_id(event.makerId())
                    .maker_name(event.makerUsername())
                    .made_on_date(Timestamp.from(event.madeOnDate()))
                    .processing_result(event.processingResult())
                    .command_as_json(payload)
                    .api_url(event.apiUrl())
                    .ip_address(event.ipAddress())
                    .error_message(event.errorMessage())
                    .browser_name(event.browserName())
                    .operating_system(event.operationSystem())
                    .operating_system_version(event.operationSystemVersion())
                    .device_model(event.device_model())
                    .build();

            repository.save(auditLog);
            log.info("Audit saved — action={} entity={} maker={}",
                    event.action(), event.entityName(), event.makerUsername());

        } catch (Exception e) {
            log.error("Audit persistence FAILED — action={} entity={} reason={}",
                    event.action(), event.entityName(), e.getMessage(), e);
        }
    }
}