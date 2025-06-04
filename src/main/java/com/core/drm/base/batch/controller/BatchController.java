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
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.core.drm.base.batch.constant.JobState.DISABLE;
import static com.core.drm.base.constant.DataStateCode.U;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobExecutionService jobExecutionService;
    private final StepExecutionService stepExecutionService;
    private final JobDefinitionService jobDefinitionService;
    private final BatchSchedulerService batchSchedulerService;

    @GetMapping("/job-executions")
    public Page<JobExecution> getJobExecutionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createTime").descending());

        return jobExecutionService.findAllByPageable(pageRequest);
    }

    @GetMapping("/step-executions")
    public Page<StepExecution> getStepExecutionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createTime").descending());
        return stepExecutionService
                .findAllByPageable(pageRequest);
    }

    @GetMapping("/jobs")
    public List<JobDefinition> getJobDefinitionList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return jobDefinitionService
                .findAllByPageable(pageRequest)
                .getContent();
    }

    @PutMapping("/batch-management/edit")
    public ResponseEntity<String> modifyBatch(@RequestBody JobDefinitionDTO jobDefinitionDTO) throws SchedulerException {
        jobDefinitionDTO.setDataCode(U.name());
        batchSchedulerService.updateSchedule(jobDefinitionDTO);
        return ResponseEntity.ok("배치 수정 완료");
    }

    @PutMapping("/batch-management/disable")
    public ResponseEntity<String> disableBatch(String jobBeanName) throws SchedulerException {
        JobDefinitionDTO jobDefinitionDTO = jobDefinitionService.findByJobBeanName(jobBeanName).toDTO();
        jobDefinitionDTO.setState(DISABLE.name());
        batchSchedulerService.deleteSchedule(jobDefinitionDTO);
        log.debug(jobDefinitionDTO.toString());
        return ResponseEntity.ok("배치 삭제 완료");
    }

    @GetMapping("/batch-status/daily")
    public List<BatchStatusDTO> countBatchStatusDaily() {
        return jobExecutionService.countBatchStatus();
    }


}
