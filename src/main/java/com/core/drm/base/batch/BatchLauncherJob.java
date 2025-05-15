package com.core.drm.base.batch;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.service.JobDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchLauncherJob implements Job {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobDefinitionService jobDefinitionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String jobBeanName = jobExecutionContext.getJobDetail().getKey().getName();

        try {
            JobDefinition jobDefinition = jobDefinitionService.findByJobBeanName(jobBeanName);
            JobParameters jobParameters = initParams(jobDefinition);
            org.springframework.batch.core.Job job = jobRegistry.getJob(jobDefinition.getJobBeanName());

            jobLauncher.run(job, jobParameters);
        } catch (NoSuchJobException e) {
            //TODO: 상수처리
            throw new JobExecutionException("[ERROR] job이 존재하지 않음");
        } catch (Exception e) {
            throw new JobExecutionException("[ERROR] job 실행중 예외 발생", e);
        }
    }

    private JobParameters initParams(JobDefinition jobDefinition) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobDefinition.getJobParams().forEach(jobParametersBuilder::addString);
        jobParametersBuilder.addLong("timestamp", System.currentTimeMillis());
        return jobParametersBuilder.toJobParameters();
    }
}
