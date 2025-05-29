package com.core.drm.base.batch.dto;

import com.core.drm.base.batch.constant.JobState;
import com.core.drm.base.batch.exception.BatchException;
import com.core.drm.base.constant.DataStateCode;
import com.core.drm.base.batch.domain.JobDefinition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.quartz.CronExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.core.drm.base.batch.constant.CronExpressionConst.EVERY_5_MINUTES;
import static com.core.drm.base.batch.constant.JobState.ENABLE;
import static com.core.drm.base.batch.constant.errormessage.BatchExceptionMessage.INVALID_VALUE;
import static com.core.drm.base.constant.DataStateCode.I;

@Getter
@Setter
public class JobDefinitionDTO {

    private String jobBeanName;
    private String state;
    private String cronExpression;
    private String dataCode;
    private Map<String, String> jobParams;

    @Builder
    public JobDefinitionDTO(
            String jobBeanName,
            String state,
            String cronExpression,
            String dataCode,
            Map<String, String> jobParams) {
        validateDTO(state, cronExpression, dataCode);
        this.jobBeanName = jobBeanName;
        this.state = state;
        this.cronExpression = cronExpression;
        this.dataCode = dataCode;
        this.jobParams = jobParams;
    }

    private void validateDTO(
            String state,
            String cronExpression,
            String dataCode) {
        validateState(state);
        validateCronExpression(cronExpression);
        validateDateCode(dataCode);
    }

    private void validateState(String state) {
        boolean valid = Arrays.stream(JobState.values())
                .anyMatch(s -> s.name().equals(state));
        if (!valid) {
            throw new BatchException(INVALID_VALUE, state);
        }
    }

    private void validateCronExpression(String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new BatchException(INVALID_VALUE, cronExpression);
        }
    }

    private void validateDateCode(String dateCode) {
        boolean valid = Arrays.stream(DataStateCode.values())
                .anyMatch(dc -> dc.name().equals(dateCode));
        if (!valid) {
            throw new BatchException(INVALID_VALUE, dateCode);
        }
    }

    public JobDefinition toEntity() {
        return JobDefinition.builder()
                .jobBeanName(jobBeanName)
                .cronExpression(cronExpression)
                .state(state)
                .dataCode(dataCode)
                .jobParams(jobParams)
                .build();
    }

    public JobDefinitionDTO initDTO() {
        this.state = Optional.ofNullable(state).orElse(ENABLE.name());
        this.dataCode = Optional.ofNullable(dataCode).orElse(I.name());
        this.jobParams = Optional.ofNullable(jobParams).orElse(Collections.emptyMap());
        this.cronExpression = Optional.ofNullable(cronExpression).orElse(EVERY_5_MINUTES.getExpression());
        return this;
    }
}
