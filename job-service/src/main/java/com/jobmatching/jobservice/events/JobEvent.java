// job-service
package com.jobmatching.jobservice.events;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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
