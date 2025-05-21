package com.core.drm.admin.dto;

import com.core.drm.base.constant.DataStateCode;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class UserDTO {
    private String userId;
    @Nullable
    private String password;
    @Nullable
    private String name;
    @Nullable
    private DataStateCode dateCode;
    @Nullable
    private String passwordSalt;
    @Nullable
    private LocalDateTime createTime;
    @Nullable
    private LocalDateTime modifiedTime;
}
