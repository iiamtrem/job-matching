package com.jobmatching.jobservice.common;

import com.jobmatching.jobservice.model.AuditLogEntry;
import com.jobmatching.jobservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Pointcut("@annotation(auditLog)")
    public void annotatedWithAuditLog(AuditLog auditLog) {}

    @AfterReturning(pointcut = "annotatedWithAuditLog(auditLog)", returning = "result")
    public void logAudit(JoinPoint joinPoint, AuditLog auditLog, Object result) {
        String action = auditLog.action();
        String username = "anonymous";
        String role = "UNKNOWN";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();

            if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
                role = auth.getAuthorities().iterator().next().getAuthority();
            }
        }

        AuditLogEntry entry = new AuditLogEntry();
        entry.setAction(action);
        entry.setTimestamp(Instant.now());
        entry.setUsername(username);
        entry.setRole(role);
        entry.setService("job-service");

        auditLogRepository.save(entry);

        log.info("AUDIT: {} by {} ({})", action, username, role);
    }
}
