package com.core.drm.crypto.util;

import com.core.drm.crypto.exception.FileParserException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class FileParser {

    private FileParser() {
    }

    public static void validateOutputStreamPointer(OutputStream outputStream, int size) {
        if (outputStream instanceof ByteArrayOutputStream) {
            int nowSize = ((ByteArrayOutputStream) outputStream).size();
            if (nowSize != size) {
                throw new FileParserException(
                        String.format("[ERROR] 출력스트림 포인터 오류: 현재 위치 %d, 기댓값 %d", nowSize, size));
            }
            return;
        }
        throw new FileParserException("[ERROR] ByteArrayOutputStream 로 처리해야 합니다.");
    }

    /*
     * 파일 생성
     * 파일 시작점에 암호화된 대칭키 삽입
     * 스트림 닫지 않음
     */
    public static void addKey(OutputStream outputStream, byte[] cryptoKey) {
        //파일 최상단에 추가
        validateOutputStreamPointer(outputStream, 0);
        byte[] buffer = new byte[256]; //TODO: os레벨에서 설정가능하도록 프로퍼티로 변경
        int readLength;
        try (
                BufferedInputStream keyStream =
                     new BufferedInputStream(new ByteArrayInputStream(cryptoKey))
        ) {
            while ((readLength = keyStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
        } catch (IOException e) {
            throw new FileParserException("[ERROR] 키추가 에러", e);
        }
    }

    /*
    IV add
    대칭키 삽입후 IV 삽입
    IV에 대해선 암호화처리 하지 않음
    순서가 보장되어야 함 -> 함수 호출순서 강제할 필요 있음
     */
    public static void addIV(OutputStream outputStream, byte[] iv) {
        validateOutputStreamPointer(outputStream, 256);
        byte[] buffer = new byte[12];
        int readLength;
        try (
                BufferedInputStream keyStream =
                        new BufferedInputStream(new ByteArrayInputStream(iv))
        ) {
            while ((readLength = keyStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
        } catch (IOException e) {
            throw new FileParserException("[ERROR] iv 추가 에러", e);
        }
    }

    /*
     * 파일 파싱
     * 파일 시작점에서 암호화 대칭키 분리
     * 분리시점은 파일 읽을때 공개키사이즈만큼만 읽어서 암호화키 가져오고 -> byte[] 메모리상에서만 남김
     * 나머지는 암호화파일로 남은 스트림에 대해 복호화 처리
     * 스트림 닫지 않음
     */
    public static byte[] parseKey(InputStream inputFileStream) {
        byte[] cryptoKey = new byte[256]; // 2048비트 고정

        try {
            inputFileStream.read(cryptoKey); /* read crypto key */
            return cryptoKey;
        } catch (IOException e) {
            throw new FileParserException("[ERROR] 키 파싱에러", e);
        }
    }

    /*
    파일 파싱
    키 파싱 이후 IV 파싱
     */
    public static byte[] parseIV(InputStream inputFileStream) {
        byte[] iv = new byte[12]; // 96bit

        try {
            inputFileStream.read(iv); /* read crypto key */

            return iv;
        } catch (IOException e) {
            throw new FileParserException("[ERROR] iv 파싱에러", e);
        }
    }
}
