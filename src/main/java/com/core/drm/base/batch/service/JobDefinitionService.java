package com.core.drm.base.batch.service;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.dto.JobDefinitionDTO;
import com.core.drm.base.batch.repository.JobDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.core.drm.base.batch.constant.JobState.ENABLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDefinitionService {

    private final JobDefinitionRepository jobDefinitionRepository;

    public JobDefinition saveJobDefinition(JobDefinition jobDefinition) {
        LocalDateTime date = Optional.ofNullable(jobDefinition.getCreatedDate()).orElse(LocalDateTime.now());
        jobDefinition.setCreatedDate(date);
        return jobDefinitionRepository.save(jobDefinition);
    }

    public JobDefinition findByJobBeanName(String jobBeanName) {
        return jobDefinitionRepository.findById(jobBeanName)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<JobDefinition> findAllEnableJobs() {
        return jobDefinitionRepository.findByState(ENABLE.name());
    }

    public Page<JobDefinition> findAllByPageable(Pageable pageable) {
        return jobDefinitionRepository.findAll(pageable);
    }

    @Transactional
    public void updateJobDefinition(JobDefinitionDTO jobDefinitionDTO) {
        JobDefinition jobDefinition = findByJobBeanName(jobDefinitionDTO.jobBeanName());
        jobDefinition.setJobParams(jobDefinitionDTO.jobParams());
        jobDefinition.setDataCode(jobDefinitionDTO.dataCode());
        jobDefinition.setModifiedDate(LocalDateTime.now());
        jobDefinition.setCronExpression(jobDefinitionDTO.cronExpression());
    }

}
