package com.core.drm.crypto.domain;

import com.core.drm.crypto.constant.FileHeaderKey;
import com.core.drm.crypto.exception.FileHeaderException;

import java.util.Map;

public class CipherFileHeader {

    private final Map<FileHeaderKey, byte[]> headers;

    public CipherFileHeader(final Map<FileHeaderKey, byte[]> headers) {
        validateHeaders(headers);
        this.headers = headers;
    }

    /*
    헤더 유효성 체크
    각 요소의 길이확인
     */
    private void validateHeaders(Map<FileHeaderKey, byte[]> headers) {
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

}
