package com.core.drm.crypto.util;

import com.core.drm.crypto.constant.FileHeaderKey;
import com.core.drm.crypto.domain.CipherFileHeader;
import com.core.drm.crypto.exception.FileParserException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.core.drm.crypto.constant.FileHeaderKey.*;
import static com.core.drm.crypto.constant.errormessage.FileParserExceptionMessage.*;

@Slf4j
public class FileParser {

    private FileParser() {
    }

    public static CipherFileHeader generateHeader(byte[] sign, byte[] key, byte[] iv) {
        byte[] originSign = Optional.ofNullable(sign)
                .orElse(
                        PropertiesUtil
                                .getApplicationProperty("drm.header.signature")
                                .getBytes(StandardCharsets.UTF_8)
                );
        log.debug("sign: {}", new String(originSign));
        LinkedHashMap<FileHeaderKey, byte[]> header = new LinkedHashMap<>();
        header.put(SIGNATURE, originSign);
        header.put(KEY, key);
        header.put(IV, iv);

        return new CipherFileHeader(header);
    }

    /*
    헤더 추가
     */
    public static void addHeader(OutputStream outputStream, CipherFileHeader header) {
        byte[] headerBytes = header.getBytes();
        byte[] buffer = new byte[1024];
        int readLength;
        try (
                BufferedInputStream keyStream =
                        new BufferedInputStream(new ByteArrayInputStream(headerBytes))
        ) {
            while ((readLength = keyStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
        } catch (IOException e) {
            throw new FileParserException(FAIL_ADD_HEADER, e);
        }
    }

    public static CipherFileHeader parseHeader(InputStream inputStream) {
        byte[] sign = parseSign(inputStream);
        byte[] key = parseKey(inputStream);
        byte[] iv = parseIV(inputStream);

        return generateHeader(sign, key, iv);
    }

    /*
    시그니처 파싱
     */
    public static byte[] parseSign(InputStream inputFileStream) {
        byte[] sign = new byte[12]; // 12byte 고정

        try {
            inputFileStream.read(sign); /* read sign */
            return sign;
        } catch (IOException e) {
            throw new FileParserException(FAIL_SIGN_PARSE, e);
        }
    }

    /*
     * 파일 파싱
     * 암호화 대칭키 분리
     * 분리시점은 파일 읽을때 공개키사이즈만큼만 읽어서 암호화키 가져오고 -> byte[] 메모리상에서만 남김
     * 나머지는 암호화파일로 남은 스트림에 대해 복호화 처리
     * 스트림 닫지 않음
     */
    private static byte[] parseKey(InputStream inputFileStream) {
        byte[] cryptoKey = new byte[256]; // 2048비트 고정

        try {
            inputFileStream.read(cryptoKey); /* read crypto key */
            return cryptoKey;
        } catch (IOException e) {
            throw new FileParserException(FAIL_KEY_PARSE, e);
        }
    }

    /*
    파일 파싱
    키 파싱 이후 IV 파싱
     */
    private static byte[] parseIV(InputStream inputFileStream) {
        byte[] iv = new byte[12]; // 96bit

        try {
            inputFileStream.read(iv); /* read crypto key */

            return iv;
        } catch (IOException e) {
            throw new FileParserException(FAIL_IV_PARSE, e);
        }
    }
}
