package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum EntityExceptionMessage {

    NOT_FOUND_ID("[ERROR] %s entity id중 %s에 해당하는 데이터 없음")
    ;

    private final String message;

    EntityExceptionMessage(final String message) {
        this.message = message;
    }

}
