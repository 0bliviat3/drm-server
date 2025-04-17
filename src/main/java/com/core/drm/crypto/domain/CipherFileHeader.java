package com.core.drm.crypto.domain;

import com.core.drm.crypto.constant.FileHeaderKey;
import com.core.drm.crypto.exception.FileHeaderException;
import com.core.drm.crypto.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class CipherFileHeader {

    /* 순서보장을 위해 linked hash map 사용 */
    private final LinkedHashMap<FileHeaderKey, byte[]> headers;

    public CipherFileHeader(final LinkedHashMap<FileHeaderKey, byte[]> headers) {
        validateHeaders(headers);
        this.headers = headers;
    }

    private void validateHeaders(Map<FileHeaderKey, byte[]> headers) {
        validateSign(headers);
        validateOrder(headers);
        validateLength(headers);
    }

    /*
    sign 유효성 체크
     */
    private void validateSign(Map<FileHeaderKey, byte[]> headers) {
        String originSign = PropertiesUtil.getApplicationProperty("drm.header.signature");
        log.debug("input sign: {}, origin sign: {}", new String(headers.get(FileHeaderKey.SIGNATURE)), originSign);
        if (!Arrays.equals(headers.get(FileHeaderKey.SIGNATURE), originSign.getBytes(StandardCharsets.UTF_8))) {
            throw new FileHeaderException("[ERROR] 시그니처 불일치");
        }
    }

    /*
    헤더 순서 확인
     */
    private void validateOrder(Map<FileHeaderKey, byte[]> headers) {
        List<FileHeaderKey> keyList = new ArrayList<>(headers.keySet());
        List<FileHeaderKey> originKeys = List.of(FileHeaderKey.values());
        if (!originKeys.equals(keyList)) {
            throw new FileHeaderException("[ERROR] 헤더 순서 불일치");
        }
    }


    /*
    헤더 유효성 체크
    각 요소의 길이확인
     */
    private void validateLength(Map<FileHeaderKey, byte[]> headers) {
        int cnt = (int) headers.keySet()
                .stream()
                .filter(key -> compareKeySize(key,headers))
                .count();
        if (cnt != headers.size()) {
            throw new FileHeaderException("[ERROR] 헤더 검증 에러: 유효한 헤더수가 일치 하지 않습니다.");
        }
    }

    private boolean compareKeySize(FileHeaderKey key, Map<FileHeaderKey, byte[]> headers) {
        return key.getLength() == headers.get(key).length;
    }

    /*
    byte[] 변환
     */
    public byte[] getBytes() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            this.headers.values().forEach(bytes -> write(outputStream, bytes));
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new FileHeaderException("[ERROR] 파일 헤더 병합 에러", e);
        }

    }

    private void write(OutputStream outputStream, byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new FileHeaderException("[ERROR] 파일 헤더 스트림 작성 에러", e);
        }
    }

    public byte[] getByte(FileHeaderKey key) {
        return this.headers.get(key);
    }

}
