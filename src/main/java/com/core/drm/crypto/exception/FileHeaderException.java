package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.FileHeaderExceptionMessage;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.ABOUT_FILE_HEADER;

@Slf4j
public class FileHeaderException extends IllegalArgumentException implements DRMException{

    public FileHeaderException() {
    }

    public FileHeaderException(FileHeaderExceptionMessage message) {
        super(message.getMessage());
        log.error("file header exception: {}", message.getMessage());
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
        log.error("file header exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file header exception: {}", formatMsg);
    }

    public FileHeaderException(FileHeaderExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs), cause);
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file header exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

    @Override
    public ResponseMessage getResponseMessage() {
        return ABOUT_FILE_HEADER;
    }
}
