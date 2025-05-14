package com.core.base.batch.service;

import com.core.base.batch.domain.JobDefinition;
import com.core.base.batch.repository.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDefinitionService {

    private final JobDefinitionRepository jobDefinitionRepository;

    public JobDefinition saveJobDefinition(JobDefinition jobDefinition) {
        jobDefinition.setCreatedDate(LocalDateTime.now());
        return jobDefinitionRepository.save(jobDefinition);
    }

    public JobDefinition findByJobBeanName(String jobBeanName) {
        return jobDefinitionRepository.findById(jobBeanName)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<JobDefinition> findAllEnableJobs() {
        //TODO: 상수처리
        return jobDefinitionRepository.findByState("enable");
    }

}
