package com.core.drm.admin.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_error_history")
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorHistory {

    @Id
    @GeneratedValue
    @Column(name = "error_id")
    private UUID errorId;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "return_message")
    private String returnMessage;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "stack_trace", columnDefinition = "LONGTEXT")
    private String stackTrace;
}
