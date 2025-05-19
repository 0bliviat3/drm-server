package com.core.drm.base.batch.service;

import com.core.drm.base.batch.BatchLauncherJob;
import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.dto.JobDefinitionDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchSchedulerService {

    private final JobRegistry jobRegistry;
    private final Scheduler scheduler;
    private final JobDefinitionService jobDefinitionService;

    public void registryAllEnableJob() throws SchedulerException {
        List<JobDefinition> jobs = jobDefinitionService.findAllEnableJobs();

        for (JobDefinition job : jobs) {
            JobDetail jobDetail = makeJobDetail(job.toDTO());
            Trigger trigger = makeTrigger(jobDetail, job.toDTO());
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    public boolean isExistJob(JobDefinitionDTO jobDTO) {
        try {
            jobDefinitionService.findByJobBeanName(jobDTO.jobBeanName());
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public void registrySchedule(JobDefinitionDTO jobDTO) throws SchedulerException {
        if (!isExistJob(jobDTO)) {
            jobDefinitionService.saveJobDefinition(jobDTO.toEntity());
            JobDetail jobDetail = makeJobDetail(jobDTO);
            Trigger trigger = makeTrigger(jobDetail, jobDTO);
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    public void deleteSchedule(JobDefinitionDTO jobDTO) throws SchedulerException {
        jobDefinitionService.updateJobDefinition(jobDTO);
        scheduler.deleteJob(JobKey.jobKey(jobDTO.jobBeanName()));
    }

    public void updateSchedule(JobDefinitionDTO jobDTO) throws SchedulerException {
        deleteSchedule(jobDTO);
        JobDetail jobDetail = makeJobDetail(jobDTO);
        Trigger trigger = makeTrigger(jobDetail, jobDTO);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail makeJobDetail(JobDefinitionDTO jobDefinitionDTO) {
        return JobBuilder.newJob(BatchLauncherJob.class)
                .withIdentity(jobDefinitionDTO.jobBeanName())
                .storeDurably()
                .build();
    }

    private Trigger makeTrigger(JobDetail jobDetail, JobDefinitionDTO jobDefinitionDTO) {
        //TODO: 상수 처리
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("trigger-" + jobDefinitionDTO.jobBeanName())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinitionDTO.cronExpression()))
                .build();
    }

    /*
    DB에 bean이 존재할 경우 init하지 않음
    존재하지 않을 경우 최초 bean 삽입
     */
    public void initJob() {
        List<JobDefinitionDTO> jobDefinitionDTOS = getUnRegisteredJobList();
        if (jobDefinitionDTOS.isEmpty()) {
            return;
        }
        jobDefinitionDTOS
                .forEach(jobDTO -> jobDefinitionService.saveJobDefinition(jobDTO.toEntity()));
    }

    private List<JobDefinitionDTO> getUnRegisteredJobList() {
        //TODO: 상수처리 필요
        Set<String> jobDefinitions = jobDefinitionService.findAllEnableJobs()
                .stream()
                .map(JobDefinition::getJobBeanName)
                .collect(Collectors.toSet());
        return jobRegistry.getJobNames()
                .stream()
                .filter(jobBeanName -> !jobDefinitions.contains(jobBeanName))
                .map(jobBeanName ->
                        new JobDefinitionDTO(
                                jobBeanName,
                                "ENABLE",
                                "0 0/5 * 1/1 * ? *",
                                "I",
                                Collections.emptyMap()
                        )
                )
                .toList();
    }
}
