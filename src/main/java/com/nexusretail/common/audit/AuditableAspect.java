package com.nexusretail.common.audit;

import com.nexusretail.common.annotation.Auditable;
import com.nexusretail.data.models.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditableAspect {

    private final ApplicationEventPublisher eventPublisher;
    private final SpelExpressionParser spelParser = new SpelExpressionParser();

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        Object result          = null;
        String processingResult = "SUCCESS";
        String errorMessage     = null;

        try {
            result = pjp.proceed();  // execute the real method
            return result;
        } catch (Exception ex) {
            processingResult = "FAILED";
            errorMessage     = ex.getMessage();
            throw ex;               // re-throw — never swallow
        } finally {
            try {
                Long entityId = resolveEntityId(auditable.entityIdSpEL(), pjp, result);
                AuditEvent event = buildEvent(auditable, entityId, pjp.getArgs(),
                        result, processingResult, errorMessage);
                eventPublisher.publishEvent(event); // fast, in-memory, non-blocking
            } catch (Exception e) {
                // Audit failure MUST NOT affect business transaction
                log.warn("Audit publish failed for action={}", auditable.action(), e);
            }
        }
    }

    private Long resolveEntityId(String spel, ProceedingJoinPoint pjp, Object result) {
        if (!StringUtils.hasText(spel)) return null;
        try {
            MethodSignature sig     = (MethodSignature) pjp.getSignature();
            var context             = new StandardEvaluationContext();
            String[] paramNames     = sig.getParameterNames();
            Object[] args           = pjp.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            context.setVariable("result", result);
            return spelParser.parseExpression(spel).getValue(context, Long.class);
        } catch (Exception e) {
            log.warn("SpEL resolution failed: {}", spel);
            return null;
        }
    }

    private AuditEvent buildEvent(Auditable auditable, Long entityId, Object[] args,
                                  Object result, String processingResult, String errorMessage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest req = getRequest();

        return AuditEvent.builder()
                .action(auditable.action())
                .entityName(auditable.entity())
                .entityId(entityId)
                .commandArgs(args)
                .result(result)
                .processingResult(processingResult)
                .errorMessage(errorMessage)
                .makerUsername(auth != null ? auth.getName() : "system")
                .makerId(extractUserId(auth))
                .apiUrl(req != null ? req.getRequestURI() : null)
                .ipAddress(req != null ? req.getRemoteAddr() : null)
                .madeOnDate(Instant.now())
                .build();
    }

    private HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) { return null; }
    }

    private Long extractUserId(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User user)) return null;
        return user.getId();
    }
}