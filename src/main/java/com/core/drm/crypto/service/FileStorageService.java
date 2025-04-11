package com.core.drm.crypto.service;

import com.core.drm.crypto.domain.CipherFile;

/*
리턴을 스트리밍 방식으로 처리할지
클라우드 URL로 처리할지 미정이므로 추상화한다.
 */
public interface FileStorageService {

    public CipherFile responseFile();

}
