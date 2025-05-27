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
            bse.step_execution_id,
        	bse.step_name,
        	bse.create_time,
        	bse.start_time,
        	bse.end_time,
        	bse.status,
        	bse.commit_count,
        	bse.read_count,
        	bse.filter_count,
        	bse.write_count,
        	bse.read_skip_count,
        	bse.write_skip_count,
        	bse.process_skip_count,
        	bse.rollback_count,
        	bse.exit_code,
        	bse.exit_message,
        	bse.last_updated
        FROM batch_step_execution bse
        """)
@Getter
public class StepExecution {

    @Id
    @Column(name = "step_execution_id", length = 8)
    private Integer stepExecutionId;

    @Column(name = "step_name", length = 100)
    private String stepName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "commit_count", length = 8)
    private Integer commitCount;

    @Column(name = "read_count", length = 8)
    private Integer readCount;

    @Column(name = "filter_count", length = 8)
    private Integer filterCount;

    @Column(name = "write_count", length = 8)
    private Integer writeCount;

    @Column(name = "read_skip_count", length = 8)
    private Integer readSkipCount;

    @Column(name = "process_skip_count", length = 8)
    private Integer processSkipCount;

    @Column(name = "rollback_count", length = 8)
    private Integer rollbackCount;

    @Column(name = "exit_code", length = 2500)
    private String exitCode;

    @Column(name = "exit_message", length = 2500)
    private String exitMessage;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}
