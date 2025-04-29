package com.core.drm.crypto.constant.errormessage;


import lombok.Getter;

@Getter
public enum PropertyExceptionMessage {
    INVALID_PROPERTY("[ERROR] 등록되지 않은 프로퍼티"),
    FAIL_LOAD("[ERROR] %s 로드 실패")
    ;
    private final String message;

    PropertyExceptionMessage(final String message) {
        this.message = message;
    }
}
