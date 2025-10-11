package com.jobmatching.jobservice.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobPayload {
    private String id;
    private String employerId;
    private String title;
    private String description;
    private String requirements;
    private Integer salaryMin;
    private Integer salaryMax;
    private String location;
    private String jobType;
    private String status;
}
