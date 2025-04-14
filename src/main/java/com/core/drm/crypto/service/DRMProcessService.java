package com.core.drm.crypto.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/*
애플리케이션 레이어 서비스
DRM 서비스의 도메인을 생성하고, 비즈니스로직을 결합한다
이 레이어는 개발중 구조가 바뀔수 있으므로 추상화 하고 따로 구현한다.
리턴타입에 대해선 좀 더 고민이 필요함
 */
public interface DRMProcessService {

    /*
    암호화
     */
    public InputStream encryptFile(MultipartFile file);


    /*
    복호화
     */
    public InputStream decryptFile(MultipartFile file);

}
