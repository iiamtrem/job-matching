package com.jobmatching.searchservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Document(indexName = "jobs")
@Setting(settingPath = "es-settings.json")
public class JobDocument {

    @Id
    private Long id;

    private Long employerId;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String requirements;

    @Field(type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Keyword)
    private String jobType;

    @Field(type = FieldType.Double)
    private Double salaryMin;

    @Field(type = FieldType.Double)
    private Double salaryMax;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant createdAt;
}
