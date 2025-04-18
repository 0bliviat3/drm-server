package com.core.drm.crypto.constant.errormessage;

import lombok.Getter;

/*
응답용 에러 메세지
구체적인 에러 내용에 대해서는 관리자 페이지에서만 처리하고
클라이언트에겐 이 메세지로 래핑해서 전달한다.
 */
@Getter
public enum ResponseMessage {

    ABOUT_CIPHER("암호화 오류", "D901"),
    ABOUT_FILE("파일 오류", "D902"),
    ABOUT_FILE_HEADER("파일 헤더 오류", "D903"),
    ABOUT_FILE_PARSER("파일 파싱 오류", "D904"),
    ABOUT_KEY("암복호화 키 오류", "D905"),
    UNCHECKED_EXCEPTION("미분류 오류", "D1000")
    ;

    private final String message;
    private final String code;

    ResponseMessage(final String message, final String code) {
        this.message = message;
        this.code = code;
    }

}
