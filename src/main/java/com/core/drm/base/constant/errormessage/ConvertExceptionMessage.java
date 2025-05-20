package com.core.drm.base.constant.errormessage;

import lombok.Getter;

@Getter
public enum ConvertExceptionMessage {
    FAIL_CONVERT_COLUMN("[ERROR] 컬럼 변환 실패"),
    FAIL_CONVERT_MAP("[ERROR] MAP 변환 실패")
    ;

    private final String message;

    ConvertExceptionMessage(final String message) {
        this.message = message;
    }
}
