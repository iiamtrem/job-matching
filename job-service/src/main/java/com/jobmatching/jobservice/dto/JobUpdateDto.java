package com.jobmatching.jobservice.dto;

import com.jobmatching.jobservice.model.enums.JobType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter
public class JobUpdateDto {
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String location;
    private JobType jobType;
    private Set<SkillDto> skills;
}
