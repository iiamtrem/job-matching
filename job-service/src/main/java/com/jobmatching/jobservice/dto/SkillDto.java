package com.jobmatching.jobservice.dto;

import com.jobmatching.jobservice.model.enums.SkillLevel;
import lombok.Data;

@Data
public class SkillDto {
    private String skillName;
    private SkillLevel requiredLevel;
}