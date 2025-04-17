package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum CipherExceptionMessage {

    FAIL_INIT_STREAM("[ERROR] 입출력 스트림이 초기화되지 않음"),
    FAIL_INIT_CIPHER("[ERROR] cipher 객체가 초기화되지 않음"),
    FAIL_ENCRYPT("[ERROR] 파일 암호화 오류"),
    FAIL_DECRYPT("[ERROR] 파일 복호화 오류"),
    FAIL_ENCRYPT_KEY("[ERROR] 대칭키 비대칭 암호화 실패"),
    FAIL_DECRYPT_KEY("[ERROR] 대칭키 비대칭 복호화 실패"),
    IMPOSSIBLE_TRY("[ERROR] 처리불가능한 요청: %s 문서에 %s 시도"),
    INVALID_SIGN("[ERROR] 시그니처 검증 에러");

    private final String message;

    CipherExceptionMessage(String message) {
        this.message = message;
    }
}
