package com.core.drm.crypto.exception;

import lombok.extern.slf4j.Slf4j;

/*
암복호화 과정중 발생하는 예외
 */
@Slf4j
public class CipherException extends IllegalStateException {

    public CipherException() {
    }

    public CipherException(String message) {
        log.error("cipher exception: {}", message);
    }

    public CipherException(String message, Throwable cause) {
        log.error("cipher exception: {}, cause: {}", message, cause.getMessage());
    }

}
