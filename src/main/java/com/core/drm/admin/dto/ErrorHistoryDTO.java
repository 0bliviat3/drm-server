package com.core.drm.admin.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record ErrorHistoryDTO(
        @Nullable UUID errorId,
        String errorCode,
        String errorMessage,
        String returnMessage,
        LocalDateTime eventTime,
        String stackTrace
) {
}
