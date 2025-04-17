package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

@Getter
public enum FileExceptionMessage {
    NOT_SUPPORT_EXTENSION("[ERROR] %s는 확장자 화이트리스트에 없습니다."),
    FAIL_TAMP_SAVE("[ERROR] 파일 임시저장 에러"),
    NOT_EXISTS("[ERROR] 파일이 존재하지 않습니다."),
    FAIL_CONVERT_STREAM("[ERROR] 암호화파일 객체 스트림 변환 에러"),
    WRONG_RETURN_TYPE("[ERROR] 리턴타입 에러: 입력스트림을 기대하나 %s 으로 지정함")
    ;

    private final String message;

    FileExceptionMessage(String message) {
        this.message = message;
    }
}
