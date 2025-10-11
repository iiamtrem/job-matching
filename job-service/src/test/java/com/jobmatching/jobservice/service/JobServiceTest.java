package com.jobmatching.jobservice.service;

import com.jobmatching.jobservice.dto.JobUpdateDto;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.model.enums.JobType;
import com.jobmatching.jobservice.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Test
    void updateJob_changesTitle_ok() {
        // arrange
        JobRepository repo = mock(JobRepository.class);
        JobService svc = new JobService(repo);

        Job existing = new Job();
        existing.setId(1L);
        existing.setEmployerId(10000L);
        existing.setTitle("Java");
        existing.setJobType(JobType.FULL_TIME);
        existing.setLocation("HCM");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        JobUpdateDto dto = new JobUpdateDto();
        dto.setTitle("Senior Java"); // chỉ set field cần đổi

        // act
        Job saved = svc.updateJob(1L, dto, 10000L);

        // assert
        ArgumentCaptor<Job> cap = ArgumentCaptor.forClass(Job.class);
        verify(repo).save(cap.capture());
        assertThat(cap.getValue().getTitle()).isEqualTo("Senior Java");
        assertThat(saved.getTitle()).isEqualTo("Senior Java");
    }
}
