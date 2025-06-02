package com.core.drm.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestCountDTO {

    private String date;
    private Long count;
}
