package com.jobmatching.jobservice.service;

import com.jobmatching.jobservice.dto.JobCreationDto;
import com.jobmatching.jobservice.dto.JobUpdateDto;
import com.jobmatching.jobservice.dto.SkillDto;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.model.JobSkill;
import com.jobmatching.jobservice.model.JobSkillId;
import com.jobmatching.jobservice.model.enums.SkillLevel;
import com.jobmatching.jobservice.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    /* ===================== CREATE ===================== */

    @Transactional
    public Job createJob(JobCreationDto dto, Long employerId) {
        // Tạo Job
        Job job = new Job();
        job.setEmployerId(employerId);
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setLocation(dto.getLocation());
        job.setJobType(dto.getJobType());

        // Lưu lần 1 để có jobId cho composite key JobSkillId
        Job saved = jobRepository.save(job);

        // Gắn skills nếu có
        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            Set<JobSkill> skills = mapSkillDtosToEntities(dto.getSkills(), saved);
            saved.setJobSkills(skills);
            saved = jobRepository.save(saved);
        }
        return saved;
    }

    /* ===================== READ ===================== */

    @Transactional(readOnly = true)
    public Job getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Job> listJobs(Long employerId, Pageable pageable) {
        if (employerId == null) {
            return jobRepository.findAll(pageable);
        }
        return jobRepository.findAllByEmployerId(employerId, pageable);
    }

    /* ===================== UPDATE ===================== */

    @Transactional
    public Job updateJob(Long id, JobUpdateDto dto, Long employerId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));

        // Chỉ chủ job mới được sửa
        if (!job.getEmployerId().equals(employerId)) {
            throw new IllegalStateException("You are not allowed to update this job");
        }

        if (dto.getTitle() != null)        job.setTitle(dto.getTitle());
        if (dto.getDescription() != null)  job.setDescription(dto.getDescription());
        if (dto.getRequirements() != null) job.setRequirements(dto.getRequirements());
        if (dto.getSalaryMin() != null)    job.setSalaryMin(dto.getSalaryMin());
        if (dto.getSalaryMax() != null)    job.setSalaryMax(dto.getSalaryMax());
        if (dto.getLocation() != null)     job.setLocation(dto.getLocation());
        if (dto.getJobType() != null)      job.setJobType(dto.getJobType());

        // Nếu truyền skills -> replace toàn bộ (đơn giản cho Phase 1)
        if (dto.getSkills() != null) {
            if (job.getJobSkills() == null) {
                job.setJobSkills(new HashSet<>());
            } else {
                job.getJobSkills().clear();
            }
            Set<JobSkill> skills = mapSkillDtosToEntities(dto.getSkills(), job);
            job.getJobSkills().addAll(skills);
        }

        return jobRepository.save(job);
    }

    /* ===================== DELETE ===================== */

    @Transactional
    public void deleteJob(Long id, Long employerId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));

        if (!job.getEmployerId().equals(employerId)) {
            throw new IllegalStateException("You are not allowed to delete this job");
        }
        jobRepository.delete(job);
    }

    /* ===================== Helper ===================== */

    private Set<JobSkill> mapSkillDtosToEntities(Set<SkillDto> dtos, Job job) {
        if (dtos == null || dtos.isEmpty()) return new HashSet<>();
        return dtos.stream().map(s -> {
            JobSkill js = new JobSkill();
            js.setId(new JobSkillId(job.getId(), s.getSkillName()));
            js.setRequiredLevel(s.getRequiredLevel() == null ? SkillLevel.INTERMEDIATE : s.getRequiredLevel());
            js.setJob(job);
            return js;
        }).collect(Collectors.toSet());
    }
}
