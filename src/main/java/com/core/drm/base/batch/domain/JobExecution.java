package com.core.drm.base.batch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("""
        SELECT
            bje.job_execution_id,
        	bji.job_name,
        	bje.create_time,
        	bje.start_time,
        	bje.end_time,
        	bje.status,
        	bje.exit_code,
        	bje.exit_message,
        	bje.last_updated
        FROM batch_job_execution bje
        JOIN batch_job_instance bji
        ON bje.job_instance_id = bji.job_instance_id
        """)
@Getter
public class JobExecution {

    @Id
    @Column(name = "job_execution_id", length = 8)
    private Integer jobExecutionId;

    @Column(name = "job_name", length = 100)
    private String jobName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "exit_code", length = 2500)
    private String exitCode;

    @Column(name = "exit_message", length = 2500)
    private String exitMessage;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
