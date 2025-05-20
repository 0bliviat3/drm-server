package com.core.drm.base.batch.constant.errormessage;

import lombok.Getter;

@Getter
public enum BatchExceptionMessage {
    FAIL_BULK_PROCESS("[ERROR] 벌크 처리 실패"),
    NOT_FOUND_JOB("[ERROR] job이 존재하지 않음"),
    EXEC_JOB_ERR("[ERROR] job 실행중 예외 발생")
    ;

    private final String message;

    BatchExceptionMessage(final String message) {
        this.message = message;
    }
}
