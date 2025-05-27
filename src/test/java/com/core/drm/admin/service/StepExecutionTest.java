package com.core.drm.admin.service;

import com.core.drm.base.batch.domain.StepExecution;
import com.core.drm.base.batch.service.StepExecutionService;
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
public class StepExecutionTest {

    @Autowired
    private StepExecutionService stepExecutionService;

    @Test
    void step_실행로그_확인하기() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<StepExecution> pages = stepExecutionService.findAllByPageable(pageRequest);
        pages.forEach(it -> System.out.println(it.getStepName() + ": " + it.getExitMessage()));
    }
}
