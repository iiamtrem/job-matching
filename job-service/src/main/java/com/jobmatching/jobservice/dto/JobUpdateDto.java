package com.jobmatching.jobservice.dto;

import com.jobmatching.jobservice.model.enums.JobType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class JobUpdateDto {
    private String title;
    private String description;
    private String requirements;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String location;
    private JobType jobType;
    private Set<SkillDto> skills; // optional: nếu null thì giữ nguyên
}
