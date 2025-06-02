package com.core.drm.base.batch.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BatchStatusDTO {

    private String status;
    private Long count;
}
