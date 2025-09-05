package com.jobmatching.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "user_job_preferences")
@Data
public class UserJobPreferences {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferred_locations")
    private String preferredLocations;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferred_job_types")
    private String preferredJobTypes;

    @Column(name = "min_salary_expectation")
    private BigDecimal minSalaryExpectation;

    @Column(name = "actively_looking", nullable = false)
    private boolean activelyLooking = true;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}