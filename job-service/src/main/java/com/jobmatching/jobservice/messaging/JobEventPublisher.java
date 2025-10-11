package com.jobmatching.jobservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobEventPublisher {
    private final KafkaTemplate<String, JobEvent> kafkaTemplate;

    public void publish(JobEvent event) {
        kafkaTemplate.send(KafkaProducerConfig.TOPIC, event.getJobId(), event);
    }
}
