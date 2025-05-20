package com.core.drm.admin.dto;

import com.core.drm.base.constant.DataStateCode;
import jakarta.annotation.Nullable;

public record UserDTO(
        String userId,
        @Nullable String password,
        @Nullable String name,
        DataStateCode dateCode
) {
}
