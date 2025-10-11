package com.jobmatching.searchservice.api;

import com.jobmatching.searchservice.document.JobDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ElasticsearchOperations operations;

    @GetMapping("/jobs/{id}")
    public JobDocument findById(@PathVariable String id) {
        return operations.get(id, JobDocument.class);
    }

    @GetMapping("/jobs")
    public SearchHits<JobDocument> search(@RequestParam("q") String q) {
        Criteria c = new Criteria("title").matches(q)
                .or(new Criteria("description").matches(q))
                .or(new Criteria("company").is(q))
                .or(new Criteria("location").is(q));
        return operations.search(new CriteriaQuery(c), JobDocument.class);
    }
}
