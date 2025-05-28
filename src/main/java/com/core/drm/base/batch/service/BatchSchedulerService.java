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

import static com.core.drm.base.batch.constant.CronExpressionConst.EVERY_5_MINUTES;
import static com.core.drm.base.batch.constant.JobState.DISABLE;
import static com.core.drm.base.batch.constant.JobState.ENABLE;
import static com.core.drm.base.constant.DataStateCode.I;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchSchedulerService {

    private static final String TRIGGER_SALT = "trigger-";

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
        //TODO: dto 변환 로직 추가해서 기존 방식 수정할것
        //현재 논리적 오류있음 -> dto 변환 방식에서 하드코딩값 강제 삽입중
        //최초기동시엔 강제삽입이 필요한 경우가 있지만, 재기동시엔 기존값을 불러와야 함
        Set<String> jobDefinitions = jobDefinitionService.findAllEnableJobs()
                .stream()
                .map(JobDefinition::getJobBeanName)
                .collect(Collectors.toSet());
        return jobRegistry.getJobNames()
                .stream()
                .filter(jobBeanName -> !jobDefinitions.contains(jobBeanName))
                .map(jobBeanName ->
                        JobDefinitionDTO.builder()
                                .jobBeanName(jobBeanName)
                                .state(ENABLE.name())
                                .dataCode(I.name())
                                .jobParams(Collections.emptyMap())
                                .cronExpression(EVERY_5_MINUTES.getExpression())
                                .build()
                )
                .toList();
    }
}
