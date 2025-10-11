package com.jobmatching.searchservice.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)  private String employerId;   // <-- thay cho company
    @Field(type = FieldType.Text)     private String title;
    @Field(type = FieldType.Text)     private String description;
    @Field(type = FieldType.Text)     private String requirements;

    @Field(type = FieldType.Integer)  private Integer salaryMin;
    @Field(type = FieldType.Integer)  private Integer salaryMax;

    @Field(type = FieldType.Keyword)  private String location;
    @Field(type = FieldType.Keyword)  private String jobType;      // nếu là enum -> .name()
    @Field(type = FieldType.Keyword)  private String status;       // nếu có
}
