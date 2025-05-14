package com.core.base.batch.repository;

import com.core.base.batch.domain.JobDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobDefinitionRepository extends JpaRepository<JobDefinition, String> {
    List<JobDefinition> findByState(String state);
}
