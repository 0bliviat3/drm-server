package com.core.drm.admin.service;

import com.core.drm.base.batch.domain.JobExecution;
import com.core.drm.base.batch.service.JobExecutionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class JobExecutionServiceTest {

    @Autowired
    private JobExecutionService jobExecutionService;

    @Test
    void 페이징처리된_작업이력_조회하기() {
        PageRequest pageRequest = PageRequest.of(1, 5);
        Page<JobExecution> jobPages = jobExecutionService.findAllByPageable(pageRequest);
        jobPages.forEach(it -> {
            System.out.println("status: " + it.getStatus());
            System.out.println("message: " + it.getExitMessage());
        });
    }
}
