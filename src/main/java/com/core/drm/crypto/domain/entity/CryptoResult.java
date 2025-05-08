package com.core.drm.crypto.domain.entity;

import com.core.drm.crypto.constant.ProcessState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_crypto_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoResult {

    @Id
    @Column(name = "crypto_result_id")
    private UUID cryptoResultId;

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    private FileRequest fileRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    private ProcessState processState;

    @Column(nullable = false, name = "retry_count")
    private Integer retryCount;

    @Column(nullable = false, name = "process_end_time")
    private LocalDateTime processEndTime;
}
