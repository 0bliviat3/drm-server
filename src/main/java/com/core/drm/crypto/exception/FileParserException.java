package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.FileParserExceptionMessage;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.ABOUT_FILE_PARSER;

/*
파일 파싱 관련 예외
 */
@Slf4j
public class FileParserException extends IllegalStateException implements DRMException{

    public FileParserException() {
    }

    public FileParserException(FileParserExceptionMessage message) {
        super(message.getMessage());
        log.error("File parse exception: {}", message.getMessage());
    }

    public FileParserException(FileParserExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
        log.error("File parse exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public FileParserException(FileParserExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("File parse exception: {}", formatMsg);
    }

    public FileParserException(FileParserExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs), cause);
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("File parse exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

    @Override
    public ResponseMessage getResponseMessage() {
        return ABOUT_FILE_PARSER;
    }
}
