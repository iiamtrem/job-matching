package com.jobmatching.userservice.model;

import com.jobmatching.userservice.model.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;

@Entity
@Table(name = "employer_verifications")
@Data
public class EmployerVerification {

    @Id
    @Column(name = "employer_id")
    private Long employerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "employer_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private String documents;
}