package com.jobmatching.jobservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable // Đánh dấu đây là một class có thể được nhúng vào Entity khác
@Data
@EqualsAndHashCode
public class JobSkillId implements Serializable {

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "skill_name")
    private String skillName;
}