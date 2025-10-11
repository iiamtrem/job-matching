package com.jobmatching.searchservice.dto;

import lombok.Data;

@Data
public class JobUpsertRequest {
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
