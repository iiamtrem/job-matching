package com.jobmatching.jobservice.model;

import com.jobmatching.jobservice.model.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "job_skills")
@Getter @Setter
public class JobSkill {

    @EmbeddedId
    private JobSkillId id;

    @JsonIgnore
    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_level", length = 20)
    private SkillLevel requiredLevel = SkillLevel.INTERMEDIATE;

    @Column(name = "weight")
    private Float weight = 1.0f;
}
