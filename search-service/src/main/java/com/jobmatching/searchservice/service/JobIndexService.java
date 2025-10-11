package com.jobmatching.searchservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.jobmatching.searchservice.document.JobDocument;

@Service
@RequiredArgsConstructor
public class JobIndexService {

    private final ElasticsearchOperations operations;

    public void upsert(JobDocument doc) {
        operations.save(doc); // save = create or update
    }

    public void deleteById(String id) {
        operations.delete(id, JobDocument.class);
    }
}
