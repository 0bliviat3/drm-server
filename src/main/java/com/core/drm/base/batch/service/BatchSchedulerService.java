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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchSchedulerService {

    private static final String TRIGGER_SALT = "trigger-";

    private final JobRegistry jobRegistry;
    private final Scheduler scheduler;
    private final JobDefinitionService jobDefinitionService;

    /*
    활성화된 job들을 조회해 스케줄러에 등록한다.
     */
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
            jobDefinitionService.findByJobBeanName(jobDTO.getJobBeanName());
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
        scheduler.deleteJob(JobKey.jobKey(jobDTO.getJobBeanName()));
    }

    public void updateSchedule(JobDefinitionDTO jobDTO) throws SchedulerException {
        deleteSchedule(jobDTO);
        JobDetail jobDetail = makeJobDetail(jobDTO);
        Trigger trigger = makeTrigger(jobDetail, jobDTO);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail makeJobDetail(JobDefinitionDTO jobDefinitionDTO) {
        return JobBuilder.newJob(BatchLauncherJob.class)
                .withIdentity(jobDefinitionDTO.getJobBeanName())
                .storeDurably()
                .build();
    }

    private Trigger makeTrigger(JobDetail jobDetail, JobDefinitionDTO jobDefinitionDTO) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(TRIGGER_SALT + jobDefinitionDTO.getJobBeanName())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinitionDTO.getCronExpression()))
                .build();
    }

    /*
    bean으로 정의된 job들에 대해서 DB에 등록되어 있지 않다면 DB에 저장
    DB에 bean이 존재할 경우 init하지 않음
    존재하지 않을 경우 최초 bean 삽입
     */
    public void initJob() {
        List<JobDefinitionDTO> jobDefinitionDTOs = getUnRegisteredJobList();
        if (jobDefinitionDTOs.isEmpty()) {
            return;
        }
        jobDefinitionDTOs
                .forEach(jobDTO -> jobDefinitionService.saveJobDefinition(jobDTO.toEntity()));
    }

    private List<JobDefinitionDTO> getUnRegisteredJobList() {
        Set<String> jobBeanNames = jobDefinitionService.findAllEnableJobs()
                .stream()
                .map(JobDefinition::getJobBeanName)
                .collect(Collectors.toSet());
        return jobRegistry.getJobNames()
                .stream()
                .filter(jobBeanName -> !jobBeanNames.contains(jobBeanName)) //DB 에서 조회되지 않는 job
                .map(JobDefinitionDTO::new)
                .toList();
    }
}
