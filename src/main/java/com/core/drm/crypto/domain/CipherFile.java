package com.core.drm.crypto.domain;

import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.exception.FileException;
import com.core.drm.crypto.exception.FileParserException;
import com.core.drm.crypto.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


/*
암복호화 처리할 파일
- 유효성을 검증
- 스트림 변환
- 메타데이터 추가
TODO: 모드에 대해 정의가 필요하다 (암호화전, 암호화후, 복호화전, 복호화후)
 */
@Slf4j
public class CipherFile {

    private final File file;
    private final String mode;

    public CipherFile(final String path, final String mode) {
        File tempFile = new File(path);
        validateFile(tempFile.toPath(), mode);
        this.file = tempFile;
        this.mode = mode;
    }

    /*
    파일 암복호화 요청에 대해 파일 유효성 체크
     */
    private void validateFile(Path path, String mode) {
        if (FileUtil.isOriginFile("encrypt", path)) {
            validateOriginFile(mode);
        } else {
            validateMetaDataFile(path, mode);
        }
    }

    /*
    메타데이터 설정이 되지 않은 원본파일의 경우
    복호화 요청이 오면 에러를 리턴한다.
     */
    private void validateOriginFile(String mode) {
        if (mode.equals("false")) {
            throw new CipherException("[ERROR] 원본파일 복호화 요청");
        }
    }

    /*
    메타데이터 설정이된 파일인 경우
    암호화 모드인지 복호화 모드인지 확인함
    예외발생조건
    1. 암호화 모드일때 암호화요청
    2. 복호화 모드일때 복호화요청
     */
    private void validateMetaDataFile(Path path, String mode) {
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

    /*
    스트리밍 처리를 위해
    입력스트림으로 변환
     */
    public InputStream getInputStream() {
        try {
            return Files.newInputStream(file.toPath());
        } catch (IOException e) {
            throw new FileException("[ERROR] 암호화파일 객체 스트림 변환 에러", e);
        }
    }

    /*
    스트리밍 처리를 위해
    출력스트림으로 변환
     */
    public OutputStream getOutputStream() {
        try {
            return Files.newOutputStream(file.toPath());
        } catch (IOException e) {
            throw new FileException("[ERROR] 암호화파일 객체 스트림 변환 에러", e);
        }
    }

    /*
    파일을 URL로 리턴한다.
     */
    public String getURL() {
        return this.file.getPath();
    }

    /*
    파일 메타데이터 설정을 통해 암호화, 혹은 복호화처리됨을 표기함
     */
    public void setCryptoFlag() {
        //TODO: mode enum 처리
        log.info("set metadata mode: {}", mode);
        try {
            Files.setAttribute(file.toPath(), "user:encrypt", mode.getBytes());
        } catch (IOException e) {
            throw new FileParserException("[ERROR] 메타데이터 설정 에러", e);
        }
    }
}
