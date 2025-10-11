package com.jobmatching.jobservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class JobSkillId implements Serializable {
    private Long jobId;
    private String skillName;
}
