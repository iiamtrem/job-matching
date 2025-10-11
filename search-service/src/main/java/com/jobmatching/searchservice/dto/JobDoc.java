package com.jobmatching.searchservice.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;

public class JobDoc {
    @NotNull private Long id;
    @NotNull private Long employerId;

    @NotBlank private String title;
    @NotBlank private String description;
    private String requirements;

    @NotBlank private String location;
    @NotBlank private String jobType;   // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, REMOTE

    @PositiveOrZero(message="salaryMin must be >= 0")
    private Double salaryMin;

    @PositiveOrZero(message="salaryMax must be >= 0")
    private Double salaryMax;

    @NotBlank private String status;    // DRAFT, OPEN, CLOSED, ARCHIVED
    private List<String> skills;

    private Instant createdAt;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmployerId() { return employerId; }
    public void setEmployerId(Long employerId) { this.employerId = employerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public Double getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Double salaryMin) { this.salaryMin = salaryMin; }
    public Double getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Double salaryMax) { this.salaryMax = salaryMax; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
