package com.jobmatching.jobservice.repository;

import com.jobmatching.jobservice.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAllByEmployerId(Long employerId, Pageable pageable);
}
