package com.jobmatching.jobservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor; // Thêm import này
import lombok.Data;
import lombok.NoArgsConstructor;   // Thêm import này

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSkillId implements Serializable {

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "skill_name")
    private String skillName;
}