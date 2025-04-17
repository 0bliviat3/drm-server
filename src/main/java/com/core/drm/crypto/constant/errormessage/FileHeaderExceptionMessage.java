package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum FileHeaderExceptionMessage {

    INVALID_SIGN("[ERROR] 시그니처 불일치"),
    INVALID_HEADER_ORDER("[ERROR] 헤더 순서 불일치"),
    INVALID_HEADER_COUNT("[ERROR] 헤더 검증 에러: 유효한 헤더수가 일치 하지 않습니다."),
    FAIL_MERGE("[ERROR] 파일 헤더 병합 에러"),
    FAIL_WRITE_STREAM("[ERROR] 파일 헤더 스트림 작성 에러");

    private final String message;

    FileHeaderExceptionMessage(String message) {
        this.message = message;
    }
}
