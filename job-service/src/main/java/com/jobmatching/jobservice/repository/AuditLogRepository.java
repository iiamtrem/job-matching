package com.jobmatching.jobservice.repository;

import com.jobmatching.jobservice.model.AuditLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogEntry, Long> {
}
