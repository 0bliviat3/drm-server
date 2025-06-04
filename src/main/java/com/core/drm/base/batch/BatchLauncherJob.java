package com.core.drm.base.batch;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.exception.BatchException;
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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.core.drm.base.batch.constant.errormessage.BatchExceptionMessage.EXEC_JOB_ERR;
import static com.core.drm.base.batch.constant.errormessage.BatchExceptionMessage.NOT_FOUND_JOB;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchLauncherJob implements Job {

    private static final String TIMESTAMP = "timestamp";

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
            throw new BatchException(NOT_FOUND_JOB, e);
        } catch (Exception e) {
            throw new BatchException(EXEC_JOB_ERR, e);
        }
    }

    private JobParameters initParams(JobDefinition jobDefinition) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        Map<String, String> params = Optional.ofNullable(jobDefinition.getJobParams())
                .orElse(Collections.emptyMap());
        params.forEach(jobParametersBuilder::addString);
        jobParametersBuilder.addLong(TIMESTAMP, System.currentTimeMillis());
        return jobParametersBuilder.toJobParameters();
    }
}
