package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum FileParserExceptionMessage {

    FAIL_ADD_HEADER("[ERROR] 헤더 추가 에러"),
    FAIL_SIGN_PARSE("[ERROR] 시그니처 파싱에러"),
    FAIL_KEY_PARSE("[ERROR] 키 파싱에러"),
    FAIL_IV_PARSE("[ERROR] iv 파싱에러")
    ;

    private final String message;

    FileParserExceptionMessage(final String message) {
        this.message = message;
    }

}
