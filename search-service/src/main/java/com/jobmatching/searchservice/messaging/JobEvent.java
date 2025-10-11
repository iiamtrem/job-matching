package com.jobmatching.searchservice.messaging;

import lombok.*;
import java.time.Instant;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class JobEvent {
    public enum Type { CREATED, UPDATED, DELETED }

    private Type type;
    private String jobId;
    private String title;
    private String description;
    private String company;
    private String location;
    private Instant occurredAt;
}

