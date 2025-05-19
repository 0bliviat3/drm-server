package com.core.drm.base.batch.dto;

import com.core.drm.base.util.MapToJsonConverter;
import com.core.drm.base.batch.domain.JobDefinition;
import jakarta.annotation.Nullable;
import jakarta.persistence.Convert;

import java.util.Map;

public record JobDefinitionDTO(
        String jobBeanName,
        String state,
        String cronExpression,
        @Nullable String dataCode,
        @Convert(converter = MapToJsonConverter.class) Map<String, String> jobParams
) {
    public JobDefinition toEntity() {
        return JobDefinition.builder()
                .jobBeanName(jobBeanName)
                .cronExpression(cronExpression)
                .state(state)
                .dataCode(dataCode)
                .jobParams(jobParams)
                .build();
    }
}
