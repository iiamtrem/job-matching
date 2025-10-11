
package com.jobmatching.jobservice.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobEventPublisher {
    private final KafkaTemplate<String, JobEvent> kafka;
    private final org.springframework.core.env.Environment env;

    public void publish(JobEvent event) {
        String topic = env.getProperty("app.kafka.topic.jobs", "jobs.events");
        kafka.send(topic, event.getJobId(), event);
    }
}
