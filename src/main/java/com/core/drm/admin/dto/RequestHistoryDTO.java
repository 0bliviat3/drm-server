package com.core.drm.admin.dto;

import com.core.drm.admin.domain.User;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestHistoryDTO(
        @Nullable UUID requestId,
        String userId,
        String requestIP,
        String requestURL,
        LocalDateTime requestTime
) {
}
