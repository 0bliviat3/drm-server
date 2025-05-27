package com.core.drm.base.batch.service;

import com.core.drm.base.batch.domain.StepExecution;
import com.core.drm.base.batch.repository.StepExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StepExecutionService {

    private final StepExecutionRepository stepExecutionRepository;

    public Page<StepExecution> findAllByPageable(Pageable pageable) {
        return stepExecutionRepository.findAll(pageable);
    }

}
