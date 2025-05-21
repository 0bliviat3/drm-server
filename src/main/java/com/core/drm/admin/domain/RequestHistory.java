package com.core.drm.admin.domain;

import com.core.drm.admin.dto.RequestHistoryDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_request_history")
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RequestHistory {

    @Id
    @GeneratedValue
    @Column(name = "request_id")
    private UUID requestId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "request_ip")
    private String requestIP;

    @Column(name = "request_url")
    private String requestURL;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Transient
    public RequestHistoryDTO toDTO() {
        return new RequestHistoryDTO(
                requestId,
                user.getUserId(),
                requestIP,
                requestURL,
                requestTime);
    }

}
