package com.core.drm.base.batch.controller;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.domain.JobExecution;
import com.core.drm.base.batch.domain.StepExecution;
import com.core.drm.base.batch.dto.JobDefinitionDTO;
import com.core.drm.base.batch.service.BatchSchedulerService;
import com.core.drm.base.batch.service.JobDefinitionService;
import com.core.drm.base.batch.service.JobExecutionService;
import com.core.drm.base.batch.service.StepExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobExecutionService jobExecutionService;
    private final StepExecutionService stepExecutionService;
    private final JobDefinitionService jobDefinitionService;
    private final BatchSchedulerService batchSchedulerService;

    @GetMapping("/job-executions")
    public List<JobExecution> getJobExecutionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return jobExecutionService
                .findAllByPageable(pageRequest)
                .getContent();
    }

    @GetMapping("/step-executions")
    public List<StepExecution> getStepExecutionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return stepExecutionService
                .findAllByPageable(pageRequest)
                .getContent();
    }

    public List<JobDefinition> getJobDefinitionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return jobDefinitionService
                .findAllByPageable(pageRequest)
                .getContent();
    }



}
