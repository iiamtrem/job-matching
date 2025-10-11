package com.jobmatching.searchservice.mapper;

import com.jobmatching.searchservice.dto.JobDoc;
import com.jobmatching.searchservice.model.JobDocument;

public final class JobMapper {
    private JobMapper(){}

    public static JobDocument toDocument(JobDoc d) {
        JobDocument doc = new JobDocument();
        doc.setId(d.getId());
        doc.setEmployerId(d.getEmployerId());
        doc.setTitle(d.getTitle());
        doc.setDescription(d.getDescription());
        doc.setRequirements(d.getRequirements());
        doc.setLocation(d.getLocation());
        doc.setJobType(d.getJobType());
        doc.setSalaryMin(d.getSalaryMin());
        doc.setSalaryMax(d.getSalaryMax());
        doc.setStatus(d.getStatus());
        doc.setSkills(d.getSkills());
        doc.setCreatedAt(d.getCreatedAt());
        return doc;
    }
}
