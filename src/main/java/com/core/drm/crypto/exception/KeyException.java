package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.KeyExceptionMessage;
import lombok.extern.slf4j.Slf4j;

/*
키 관련 발생 가능한 예외
서버로그용 예외로 운영, 유지보수용으로 사용될 관리자용 예외를 처리함
외부용 예외는 전역예외처리기를 사용한다.
 */
@Slf4j
public class KeyException extends IllegalStateException{

    public KeyException() {
    }

    public KeyException(KeyExceptionMessage message) {
        super(message.getMessage());
        log.error("KeyException: {}", message.getMessage());
    }

    public KeyException(KeyExceptionMessage message, Throwable cause) {
        super(message.getMessage());
        log.error("KeyException: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public KeyException(KeyExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("KeyException: {}", formatMsg);
    }

    public KeyException(KeyExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("KeyException: {}, cause: {}", formatMsg, cause.getMessage());
    }

}
