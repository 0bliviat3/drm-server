package com.core.drm.base.exception;

import com.core.drm.base.constant.errormessage.ConvertExceptionMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertException extends IllegalArgumentException {

    public ConvertException() {
    }

    public ConvertException(ConvertExceptionMessage message) {
        super(message.getMessage());
        log.error("convert exception {}", message.getMessage());
    }
    public ConvertException(ConvertExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
        log.error("convert exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }
    public ConvertException(ConvertExceptionMessage message, Object... args) {
        super(String.format(message.getMessage(), args));
        log.error("convert exception {}", String.format(message.getMessage(), args));
    }
    public ConvertException(ConvertExceptionMessage message, Throwable cause, Object... args) {
        super(String.format(message.getMessage(), args), cause);
        log.error("convert exception: {}, cause: {}", String.format(message.getMessage(), args), cause.getMessage());
    }
}
