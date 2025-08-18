package com.jobmatching.jobservice.model;

import com.jobmatching.jobservice.model.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job_skills")
@Data
public class JobSkill {

    @EmbeddedId // Đánh dấu sử dụng một khóa chính được nhúng
    private JobSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_level")
    private SkillLevel requiredLevel;

    private Float weight;
}