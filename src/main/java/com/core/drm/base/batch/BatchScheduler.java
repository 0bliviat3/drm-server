package com.core.drm.base.batch;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.service.JobDefinitionService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchScheduler implements ApplicationRunner {

    private final Scheduler scheduler;
    private final JobDefinitionService jobDefinitionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<JobDefinition> jobs = jobDefinitionService.findAllEnableJobs();

        for (JobDefinition job : jobs) {
            JobDetail jobDetail = makeJobDetail(job);
            Trigger trigger = makeTrigger(jobDetail, job);
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    private JobDetail makeJobDetail(JobDefinition jobDefinition) {
        return JobBuilder.newJob(BatchLauncherJob.class)
                .withIdentity(jobDefinition.getJobBeanName())
                .storeDurably()
                .build();
    }

    private Trigger makeTrigger(JobDetail jobDetail, JobDefinition jobDefinition) {
        //TODO: 상수 처리
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("trigger-" + jobDefinition.getJobBeanName())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinition.getCronExpression()))
                .build();
    }
}
