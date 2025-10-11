package com.jobmatching.userservice.common;

import com.jobmatching.userservice.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogger auditLogger;

    @Before("@annotation(com.jobmatching.userservice.common.AuditLog)")
    public void logAudit(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AuditLog audit = method.getAnnotation(AuditLog.class);

        String action = audit.action();
        String actor = "UNKNOWN";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            actor = "ID=" + user.getId() + ", Role=" + user.getRole();
        }

        String details = "Method: " + method.getName();

        auditLogger.log(action, actor, details);
    }
}
