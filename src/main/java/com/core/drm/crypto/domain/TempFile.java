package com.core.drm.crypto.domain;

import com.core.drm.crypto.exception.FileException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;


/*
암복호화 처리할 파일 -> 임시저장된 파일
- 경로 유효성을 검증
- 스트림 변환

이 도메인의 역할은
임시저장 파일경로를 받아서 경로에 대한 스트림을 리턴함
기능 확장시 임시저장 파일 삭제도 처리할 예정
모드에 대해선 관리하지도 처리하지도 않음

모드에 대한 분기점을 정하는건 이 도메인에서 정하지 않고,
프로세스 서비스에서 나눈다.
이 도메인은 헤더가 존재하는지, 없는지에 대해선 확인하지 않는다.

모드에 대해선 다음과 같이 정의한다
파일 헤더가 존재 -> 즉 시그니처가 확인되면 암호화된 문서로 간주함
없다면 기본파일
시그니처가 있다면 암호화 요청이 불가
없다면 복호화 요청이 불가함
 */
@Slf4j
public class TempFile {

    private final File file;

    public TempFile(final String path) {
        File tempFile = new File(path);
        validateFile(tempFile);
        this.file = tempFile;
    }

    /*
    파일 경로에 대해 유효성 체크
     */
    private void validateFile(File file) {
        if (!file.exists()) {
            throw new FileException("[ERROR] 파일이 존재하지 않습니다.");
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
    임시저장 파일 삭제처리
    TODO: 구현, 배치로 일괄 처리할 예정
     */
    public void delete() {
    }
}
