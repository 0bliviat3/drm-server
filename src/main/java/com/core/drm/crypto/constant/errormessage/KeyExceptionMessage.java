package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum KeyExceptionMessage {

    FAIL_PEM_LOAD("[ERROR] pem 파일 읽기 실패!, 파일경로: %s"),
    FAIL_GENERATE_PUBLIC_KEY("[ERROR] 공개키 발행 실패"),
    FAIL_GENERATE_PRIVATE_KEY("[ERROR] 개인키 발생 실패"),
    FAIL_GENERATE_KEY("[ERROR] 대칭키 생성 오류")
    ;

    private final String message;

    KeyExceptionMessage(final String message) {
        this.message = message;
    }

}
