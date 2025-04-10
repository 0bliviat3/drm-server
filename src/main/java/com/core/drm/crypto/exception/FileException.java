package com.core.drm.crypto.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileException extends IllegalArgumentException {

    public FileException() {
    }

    public FileException(String message) {
        log.error("file exception: {}", message);
    }

    public FileException(String message, Throwable cause) {
        log.error("file exception: {}, cause: {}", message, cause.getMessage());
    }

}
