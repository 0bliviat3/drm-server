package com.core.drm.crypto.util;

import com.core.drm.crypto.exception.CipherException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class CryptoFileHelper {

    private CryptoFileHelper() {
    }

    /*
    파일 암복호화 요청에 대해 파일 유효성 체크
     */
    public static void validateFile(Path path, String mode) {
        validateOriginFile(path, mode);
        validateMetaDataFile(path, mode);
    }

    /*
    메타데이터 설정이 되지 않은 원본파일의 경우
    복호화 요청이 오면 에러를 리턴한다.
     */
    private static void validateOriginFile(Path path, String mode) {
        UserDefinedFileAttributeView view = Files
                .getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            if (view.list().contains("encrypt") && mode.equals("false")) {
                throw new CipherException("[ERROR] 원본파일 복호화 요청");
            }
        } catch (IOException e) {
            throw new CipherException("[ERROR] 원본파일 검사 오류", e);
        }
    }

    /*
    메타데이터 설정이된 파일인 경우
    암호화 모드인지 복호화 모드인지 확인함
    예외발생조건
    1. 암호화 모드일때 암호화요청
    2. 복호화 모드일때 복호화요청
     */
    private static void validateMetaDataFile(Path path, String mode) {
        try {
            byte[] modeByte = (byte[]) Files.getAttribute(path, "user:encrypt");
            String setMode = new String(modeByte);
            if (!setMode.equals(mode)) {
                throw new CipherException(String.format("[ERROR] %s모드 설정된 파일에 %s요청 에러", setMode, mode));
            }
        } catch (IOException e) {
            throw new CipherException("[ERROR] 메타데이터 파일 유효성 체크 에러", e);
        }
    }
}
