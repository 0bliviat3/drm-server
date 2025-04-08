package com.core.drm.crypto.exception;

import lombok.extern.slf4j.Slf4j;

/*
파일 파싱 관련 예외
 */
@Slf4j
public class FileParserException extends IllegalStateException{

    public FileParserException() {
    }

    public FileParserException(String message) {
        log.error("File parse exception: {}", message);
    }

    public FileParserException(String message, Throwable cause) {
        log.error("File parse exception: {}, cause: {}", message, cause.getMessage());
    }

}
