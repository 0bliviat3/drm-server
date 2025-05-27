package com.core.drm.base.batch.repository;

import com.core.drm.base.batch.domain.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Integer> {
}
