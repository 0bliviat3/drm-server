package com.core.drm.base.batch.repository;

import com.core.drm.base.batch.domain.StepExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepExecutionRepository extends JpaRepository<StepExecution, Integer> {
}
