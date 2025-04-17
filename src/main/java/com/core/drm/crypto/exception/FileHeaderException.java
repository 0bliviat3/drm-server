package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.FileHeaderExceptionMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileHeaderException extends IllegalArgumentException {

    public FileHeaderException() {
    }

    public FileHeaderException(FileHeaderExceptionMessage message) {
        super(message.getMessage());
        log.error("file header exception: {}", message.getMessage());
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Throwable cause) {
        super(message.getMessage());
        log.error("file header exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file header exception: {}", formatMsg);
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file header exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

}
