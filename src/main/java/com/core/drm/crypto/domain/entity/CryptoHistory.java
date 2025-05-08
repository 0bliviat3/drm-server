package com.core.drm.crypto.domain.entity;

import com.core.drm.crypto.constant.ProcessState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_crypto_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoHistory {

    @Id
    @Column(name = "crypto_id")
    private UUID cryptoId;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    private FileRequest fileRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    private ProcessState processState;

    @Column(name = "process_time")
    private LocalDateTime processTime;
}
