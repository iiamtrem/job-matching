package com.jobmatching.searchservice.repository;

import com.jobmatching.searchservice.model.JobDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, Long> { }
