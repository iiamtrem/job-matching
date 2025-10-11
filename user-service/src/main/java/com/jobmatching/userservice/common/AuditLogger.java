package com.jobmatching.userservice.common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component

public class AuditLogger {
    public void log(String action, String actor, String details) {
        log.info("[AUDIT] [{}] User: {}, Action: {}, Details: {}",
                Instant.now(), actor, action, details);
    }
}

