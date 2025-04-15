package com.core.drm.crypto.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileHeaderException extends IllegalArgumentException {

    public FileHeaderException(String message) {
        log.error("file header exception: {}", message);
    }

    public FileHeaderException(String message, Throwable cause) {
        log.error("file header exception: {}, cause: {}", message, cause.getMessage());
    }

}
