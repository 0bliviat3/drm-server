package com.core.drm.crypto.exception;

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

    public KeyException(String message) {
        //TODO: string message로 받을게 아닌 ENUM을 적용해 예외 메세지를 구분한다.
        super(message);
        log.error("KeyException: {}", message);
    }

    public KeyException(String message, Throwable cause) {
        log.error("KeyException: {}, cause: {}", message, cause.getMessage());
    }

}
