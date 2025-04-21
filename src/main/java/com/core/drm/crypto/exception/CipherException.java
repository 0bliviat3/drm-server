package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.CipherExceptionMessage;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.ABOUT_CIPHER;

/*
암복호화 과정중 발생하는 예외
 */
@Slf4j
public class CipherException extends IllegalArgumentException implements DRMException {

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

    @Override
    public ResponseMessage getResponseMessage() {
        return ABOUT_CIPHER;
    }
}
