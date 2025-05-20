package com.core.drm.base.batch;

import com.core.drm.base.batch.service.TempFileDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DRMJobConfig {

    private static final String BEAN_NAME = "delTempFile";
    private static final String STEP_NAME = "delTempFileStep";

    private final TempFileDeleteService tempFileDeleteService;

    @Bean
    public Job delTempFile(JobRepository jobRepository, Step delTempFileStep) {
        return new JobBuilder(BEAN_NAME, jobRepository)
                .start(delTempFileStep)
                .build();
    }

    @Bean
    public Step delTempFileStep(
            JobRepository jobRepository,
            Tasklet delTempFileTask,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(delTempFileTask, transactionManager)
                .build();
    }

    @Bean
    public Tasklet delTempFileTask() {
        return (contribution, chunkContext) -> {
            tempFileDeleteService.fileDeleteProcess();
            return RepeatStatus.FINISHED;
        };
    }
}
