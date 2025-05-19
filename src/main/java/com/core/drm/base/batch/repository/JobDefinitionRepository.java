package com.core.drm.base.batch.repository;

import com.core.drm.base.batch.domain.JobDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobDefinitionRepository extends JpaRepository<JobDefinition, String> {

    @Query("""
            SELECT
            tjd
            FROM JobDefinition tjd
            WHERE 1 = 1
            AND state = :state
            AND tjd.dataCode <> 'D'
            """)
    List<JobDefinition> findByState(@Param("state") String state);
}
