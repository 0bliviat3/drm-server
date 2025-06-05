package com.core.drm.base.batch.repository;

import com.core.drm.base.batch.dto.BatchStatusDTO;
import com.core.drm.base.batch.domain.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Integer> {

    @Query(value = """
            SELECT
            	status, COUNT(job_execution_id) AS count
            FROM batch_job_execution
            WHERE 1 = 1
            AND create_time > NOW() - INTERVAL '1 DAYS'
            GROUP BY status
            """, nativeQuery = true)
    List<BatchStatusDTO> countBatchStatusDaily();
}
