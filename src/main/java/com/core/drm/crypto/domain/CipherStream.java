package com.core.drm.crypto.domain;

import org.bouncycastle.crypto.BufferedBlockCipher;

import java.io.InputStream;
import java.io.OutputStream;

/*
설계 목적 인터페이스, 다형성 적용필요 없으면 구현 후에 삭제 처리
 */
public interface CipherStream {

    /*
     * 스트림을 암복호화 처리함
     * 파라미터로 스트림을 전달 받아 암복호화된 스트림의 해제까지 관리
     * 하나의 단일 암복호화만 처리함
     * CipherInputStream, CipherOutputStream 생성,소멸을 관리
     */

    public void encrypt(InputStream inputStream, OutputStream outputStream, BufferedBlockCipher cipher);
    public void decrypt(InputStream inputStream, OutputStream outputStream, BufferedBlockCipher cipher);

}
