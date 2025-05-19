package com.core.drm.base.batch;

import com.core.drm.base.batch.domain.JobDefinition;
import com.core.drm.base.batch.service.BatchSchedulerService;
import com.core.drm.base.batch.service.JobDefinitionService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchScheduleRegister implements ApplicationRunner {

    private final BatchSchedulerService batchSchedulerService;

    @Override
    public void run(ApplicationArguments args) throws SchedulerException {
        batchSchedulerService.initJob();
        batchSchedulerService.registryAllEnableJob();
    }

}
