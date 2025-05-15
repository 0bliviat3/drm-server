package com.core.drm.base.batch.domain;

import com.core.drm.base.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "t_job_definition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDefinition {

    /*
    job bean 이름,
    활성화 여부,
    cron 표현식,
    사용자 편의 표현식,
    pk,
    시작일,
    마지막 수정일,
    파라미터
     */
    @Id
    @Column(name = "job_bean_name")
    private String jobBeanName;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Column(name = "user_expression", nullable = false)
    private String userExpression;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "job_params", columnDefinition = "jsonb")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> jobParams;
}
