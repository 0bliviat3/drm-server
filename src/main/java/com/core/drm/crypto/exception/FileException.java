package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.FileExceptionMessage;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.ABOUT_FILE;

@Slf4j
public class FileException extends IllegalArgumentException implements DRMException{

    public FileException() {
    }

    public FileException(FileExceptionMessage message) {
        super(message.getMessage());
        log.error("file exception: {}", message.getMessage());
    }

    public FileException(FileExceptionMessage message, Throwable cause) {
        super(message.getMessage());
        log.error("file exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public FileException(FileExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file exception: {}", formatMsg);
    }

    public FileException(FileExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("file exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

    @Override
    public ResponseMessage getResponseMessage() {
        return ABOUT_FILE;
    }
}
