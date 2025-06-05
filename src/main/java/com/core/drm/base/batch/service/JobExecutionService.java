package com.core.drm.base.batch.service;

import com.core.drm.base.batch.dto.BatchStatusDTO;
import com.core.drm.base.batch.domain.JobExecution;
import com.core.drm.base.batch.repository.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;

    public Page<JobExecution> findAllByPageable(Pageable pageable) {
        return jobExecutionRepository.findAll(pageable);
    }

    public List<BatchStatusDTO> countBatchStatus() {
        return jobExecutionRepository.countBatchStatusDaily();
    }
}
