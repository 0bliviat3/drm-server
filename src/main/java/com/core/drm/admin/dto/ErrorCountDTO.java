package com.core.drm.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ErrorCountDTO {

    private Date date;
    private Long count;
}
