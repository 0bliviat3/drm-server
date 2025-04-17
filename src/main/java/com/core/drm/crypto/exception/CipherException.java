package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.CipherExceptionMessage;
import lombok.extern.slf4j.Slf4j;

/*
암복호화 과정중 발생하는 예외
 */
@Slf4j
public class CipherException extends IllegalStateException {

    public CipherException() {
    }

    public CipherException(CipherExceptionMessage message) {
        super(message.getMessage());
        log.error("cipher exception: {}", message.getMessage());
    }

    public CipherException(CipherExceptionMessage message, Throwable cause) {
        super(message.getMessage());
        log.error("cipher exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public CipherException(CipherExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("cipher exception: {}", formatMsg);
    }

    public CipherException(CipherExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("cipher exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

}
