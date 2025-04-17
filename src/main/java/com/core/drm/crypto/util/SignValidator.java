package com.core.drm.crypto.util;

import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.exception.CipherException;

import java.io.IOException;
import java.io.InputStream;

public class SignValidator {

    private SignValidator() {
    }

    public static void validateSign(TempFile file, boolean isEncrypt) {
        String expectSign = PropertiesUtil.getApplicationProperty("drm.header.signature");
        String cryptState = isEncrypt ? "암호화":"복호화";
        try (InputStream inputStream = file.getInputStream()) {
            String sign = new String(FileParser.parseSign(inputStream));
            if (expectSign.equals(sign) == isEncrypt) {
                /*
                암호화 요청에 임시파일의 시그니처가 일치함 -> 암호화된 문서를 암호화 시도
                복호화 요청에 임시파일의 시그니처가 일치하지 않음 -> 복호화 불가능한 문서
                 */
                throw new CipherException(String.format("[ERROR] 처리불가능한 요청: %s 문서에 %s 시도", cryptState, cryptState));
            }
        } catch (IOException e) {
            throw new CipherException("[ERROR] 시그니처 검증 에러", e);
        }
    }
}
