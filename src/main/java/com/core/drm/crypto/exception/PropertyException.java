package com.core.drm.crypto.exception;

import com.core.drm.crypto.constant.errormessage.PropertyExceptionMessage;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import static com.core.drm.crypto.constant.errormessage.PropertyExceptionMessage.INVALID_PROPERTY;
import static com.core.drm.crypto.constant.errormessage.ResponseMessage.ABOUT_PROPERTY;

@Slf4j
public class PropertyException extends IllegalArgumentException implements DRMException{

    public PropertyException() {
        super(INVALID_PROPERTY.getMessage());
    }

    public PropertyException(PropertyExceptionMessage message) {
        super(message.getMessage());
        log.error("property exception: {}", message.getMessage());
    }

    public PropertyException(PropertyExceptionMessage message, Throwable cause) {
        super(message.getMessage(), cause);
        log.error("property exception: {}, cause: {}", message.getMessage(), cause.getMessage());
    }

    public PropertyException(PropertyExceptionMessage message, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs));
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("property exception: {}", formatMsg);
    }

    public PropertyException(PropertyExceptionMessage message, Throwable cause, Object... formatArgs) {
        super(String.format(message.getMessage(), formatArgs), cause);
        String formatMsg = String.format(message.getMessage(), formatArgs);
        log.error("property exception: {}, cause: {}", formatMsg, cause.getMessage());
    }

    @Override
    public ResponseMessage getResponseMessage() {
        return ABOUT_PROPERTY;
    }
}
