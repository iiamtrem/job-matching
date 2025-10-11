package com.jobmatching.jobservice.dto;

import com.jobmatching.jobservice.model.enums.JobType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter
public class JobCreationDto {

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "description must not be blank")
    private String description;

    private String requirements;

    @PositiveOrZero(message = "salaryMin must be >= 0")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "salaryMax must be >= 0")
    private BigDecimal salaryMax;

    @NotBlank(message = "location must not be blank")
    private String location;

    @NotNull(message = "jobType is required")
    private JobType jobType;

    private Set<SkillDto> skills;
}
