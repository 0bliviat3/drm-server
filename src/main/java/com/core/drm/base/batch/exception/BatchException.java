package com.core.drm.base.batch.exception;

import com.core.drm.base.batch.constant.errormessage.BatchExceptionMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchException extends IllegalArgumentException {

    public BatchException() {
    }

    public BatchException(BatchExceptionMessage message) {
        super(message.getMessage());
        log.error("batch exception: {}", message.getMessage());
    }
    public BatchException(BatchExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
        log.error("batch exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }
    public BatchException(BatchExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        log.error("batch exception: {}", String.format(message.getMessage(), formatArgs));
    }
    public BatchException(BatchExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs), cause);
        log.error("batch exception: {}, cause: {}", String.format(message.getMessage(), formatArgs), cause.getMessage());
    }

}
