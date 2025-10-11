package com.jobmatching.jobservice.integration.search;

import com.jobmatching.jobservice.integration.search.dto.SearchJobDoc;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.model.JobSkill;

import java.util.List;

public final class SearchMapper {
    private SearchMapper() {}

    public static SearchJobDoc toDoc(Job j) {
        SearchJobDoc d = new SearchJobDoc();
        d.setId(j.getId());
        d.setEmployerId(j.getEmployerId());
        d.setTitle(j.getTitle());
        d.setDescription(j.getDescription());
        d.setRequirements(j.getRequirements());
        d.setLocation(j.getLocation());
        d.setJobType(j.getJobType() == null ? null : j.getJobType().name());
        d.setSalaryMin(j.getSalaryMin() == null ? null : j.getSalaryMin().doubleValue());
        d.setSalaryMax(j.getSalaryMax() == null ? null : j.getSalaryMax().doubleValue());
        d.setStatus(j.getStatus() == null ? null : j.getStatus().name());
        d.setCreatedAt(j.getCreatedAt());

        List<String> skills = j.getJobSkills()
                .stream()
                .map(SearchMapper::skillName)
                .distinct()
                .toList();
        d.setSkills(skills);
        return d;
    }

    private static String skillName(JobSkill js) {
        if (js.getId() != null && js.getId().getSkillName() != null) return js.getId().getSkillName();
        try { return (String) JobSkill.class.getMethod("getSkillName").invoke(js); }
        catch (Exception ignore) { return null; }
    }
}
