package com.jobmatching.searchservice.service;

import com.jobmatching.searchservice.dto.JobDoc;
import com.jobmatching.searchservice.dto.JobSearchRequest;
import com.jobmatching.searchservice.dto.PageResponse;
import com.jobmatching.searchservice.model.JobDocument; // sửa path nếu khác
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSearchService {

    private final ElasticsearchOperations esOps;

    public PageResponse<JobDoc> search(JobSearchRequest req) {
        int page = Optional.ofNullable(req.getPage()).orElse(0);
        int size = Math.min(Optional.ofNullable(req.getSize()).orElse(20), 50);

        String keyword   = trimOrNull(req.getKeyword());
        String location  = trimOrNull(req.getLocation());
        String jobType   = tryGetJobType(req);
        Integer salaryMin = tryGetSalaryMin(req);
        Integer salaryMax = tryGetSalaryMax(req);
        String sort       = trimOrNull(tryGetSort(req));

        String queryJson = buildEsQueryJson(keyword, location, jobType, salaryMin, salaryMax, sort);

        StringQuery query = new StringQuery(queryJson);

        SearchHits<JobDocument> hits = esOps.search(query, JobDocument.class);

        List<JobDoc> items = new ArrayList<>();
        for (SearchHit<JobDocument> hit : hits) {
            JobDocument d = hit.getContent();
            items.add(toDto(d));
        }
        long total = hits.getTotalHits();

        return new PageResponse<>(items, page, size, total);
    }

    public void upsert(JobDoc doc) {
        JobDocument esDoc = toDocument(doc);
        if (esDoc.getCreatedAt() == null) {
            esDoc.setCreatedAt(Instant.now());
        }
        esOps.save(esDoc);
    }

    public void delete(long id) {
        esOps.delete(String.valueOf(id), JobDocument.class);
    }

    // ================= Helpers =================

    private static String trimOrNull(String s) {
        return (s != null && !s.isBlank()) ? s.trim() : null;
    }

    private static boolean hasText(String s) {
        return StringUtils.hasText(s);
    }

    private static JobDoc toDto(JobDocument d) {
        JobDoc j = new JobDoc();
        j.setId(d.getId());
        j.setEmployerId(d.getEmployerId());
        j.setTitle(d.getTitle());
        j.setDescription(d.getDescription());
        j.setRequirements(d.getRequirements());
        j.setLocation(d.getLocation());
        j.setJobType(d.getJobType());
        j.setSalaryMin(d.getSalaryMin());
        j.setSalaryMax(d.getSalaryMax());
        j.setStatus(d.getStatus());
        j.setSkills(d.getSkills());
        j.setCreatedAt(d.getCreatedAt());
        return j;
    }

    private static JobDocument toDocument(JobDoc j) {
        JobDocument d = new JobDocument();
        d.setId(j.getId());
        d.setEmployerId(j.getEmployerId());
        d.setTitle(j.getTitle());
        d.setDescription(j.getDescription());
        d.setRequirements(j.getRequirements());
        d.setLocation(j.getLocation());
        d.setJobType(j.getJobType());
        d.setSalaryMin(j.getSalaryMin());
        d.setSalaryMax(j.getSalaryMax());
        d.setStatus(j.getStatus());
        d.setSkills(j.getSkills());
        d.setCreatedAt(j.getCreatedAt());
        return d;
    }

    // ====== Tương thích tên field khác nhau của DTO ======
    private static String tryGetJobType(JobSearchRequest req) {
        try {
            var m = req.getClass().getMethod("getJobType");
            Object v = m.invoke(req);
            return v != null ? v.toString() : null;
        } catch (Exception ignore) {
            try {
                var m = req.getClass().getMethod("getType");
                Object v = m.invoke(req);
                return v != null ? v.toString() : null;
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private static Integer tryGetSalaryMin(JobSearchRequest req) {
        try {
            var m = req.getClass().getMethod("getSalaryMin");
            Object v = m.invoke(req);
            return (Integer) v;
        } catch (Exception ignore) {
            try {
                var m = req.getClass().getMethod("getMinSalary");
                Object v = m.invoke(req);
                return (Integer) v;
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private static Integer tryGetSalaryMax(JobSearchRequest req) {
        try {
            var m = req.getClass().getMethod("getSalaryMax");
            Object v = m.invoke(req);
            return (Integer) v;
        } catch (Exception ignore) {
            try {
                var m = req.getClass().getMethod("getMaxSalary");
                Object v = m.invoke(req);
                return (Integer) v;
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private static String tryGetSort(JobSearchRequest req) {
        try {
            var m = req.getClass().getMethod("getSort");
            Object v = m.invoke(req);
            return v != null ? v.toString() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String buildEsQueryJson(String keyword,
                                    String location,
                                    String jobType,
                                    Integer salaryMin,
                                    Integer salaryMax,
                                    String sort) {

        int page = 0;
        int size = 20;

        StringBuilder mustArr = new StringBuilder();
        StringBuilder filterArr = new StringBuilder();

        if (hasText(keyword)) {
            appendCommaIfNeeded(mustArr);
            mustArr.append("""
              { "multi_match": {
                    "query": "%s",
                    "type": "best_fields",
                    "fields": ["title^3","skills^2","description","requirements"]
                 }}
            """.formatted(escapeJson(keyword)));
        }

        appendCommaIfNeeded(filterArr);
        filterArr.append("""
          { "term": { "status": "OPEN" } }
        """);

        // FILTER: location
        if (hasText(location)) {
            appendCommaIfNeeded(filterArr);
            filterArr.append("""
              { "term": { "location": "%s" } }
            """.formatted(escapeJson(location)));
        }

        // FILTER: jobType/type
        if (hasText(jobType)) {
            appendCommaIfNeeded(filterArr);
            filterArr.append("""
              { "term": { "jobType": "%s" } }
            """.formatted(escapeJson(jobType)));
        }

        // FILTER: salary range
        if (salaryMin != null) {
            appendCommaIfNeeded(filterArr);
            filterArr.append("""
              { "range": { "salaryMax": { "gte": %d } } }
            """.formatted(salaryMin));
        }
        if (salaryMax != null) {
            appendCommaIfNeeded(filterArr);
            filterArr.append("""
              { "range": { "salaryMin": { "lte": %d } } }
            """.formatted(salaryMax));
        }

        String sortJson;
        if ("score".equalsIgnoreCase(sort)) {
            sortJson = """
              [ {"_score": {"order":"desc"}}, {"createdAt":{"order":"desc"}} ]
            """;
        } else {
            sortJson = """
              [ {"createdAt":{"order":"desc"}} ]
            """;
        }

        String boolJson = """
          { "bool": {
              %s
              %s
          }}
        """.formatted(
                mustArr.length() > 0 ? "\"must\": [" + mustArr + "]" : "\"must\": []",
                filterArr.length() > 0 ? ", \"filter\": [" + filterArr + "]" : ""
        );

        // Highlight optional
        String highlight = """
          {
            "fields": {
              "title": {},
              "description": {}
            }
          }
        """;

        // Hoàn chỉnh query
        String json = """
        {
          "from": %d,
          "size": %d,
          "query": %s,
          "sort": %s,
          "highlight": %s,
          "track_total_hits": true
        }
        """.formatted(page * size, size, boolJson, sortJson, highlight);

        return json;
    }

    private static void appendCommaIfNeeded(StringBuilder sb) {
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ',') {
            sb.append(',');
        }
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
