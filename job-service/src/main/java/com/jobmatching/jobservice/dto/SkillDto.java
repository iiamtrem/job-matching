package com.jobmatching.jobservice.dto;

import com.jobmatching.jobservice.model.enums.SkillLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SkillDto {
    @NotBlank(message = "skillName must not be blank")
    private String skillName;
    private SkillLevel requiredLevel;
    private Float weight;
}
