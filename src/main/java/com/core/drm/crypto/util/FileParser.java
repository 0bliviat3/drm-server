package com.core.drm.crypto.util;

import com.core.drm.crypto.domain.asymmetric.AsymmetricCipher;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class FileParser {

    private FileParser() {
    }

    /*
     * 파일 생성
     * 파일 시작점에 암호화된 대칭키 삽입
     * 스트림 닫지 않음
     */
    public static void addKey(OutputStream outputStream, SecretKey key) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        //key 공개키 암호화 처리 -> 클래스분리 고민
        byte[] cryptoKey = new AsymmetricCipher().cryptKey(key);

        //파일 최상단에 추가
        byte[] buffer = new byte[1024]; //TODO: os레벨에서 설정가능하도록 프로퍼티로 변경
        int readLength;
        BufferedInputStream keyStream =
                new BufferedInputStream(new ByteArrayInputStream(cryptoKey));

        while ((readLength = keyStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readLength);
        }
    }

    /*
     * 파일 파싱
     * 파일 시작점에서 암호화 대칭키 분리
     * 분리시점은 파일 읽을때 공개키사이즈만큼만 읽어서 암호화키 가져오고 -> byte[] 메모리상에서만 남김
     * 나머지는 암호화파일로 남은 스트림에 대해 복호화 처리
     * 스트림 닫지 않음
     */
    public static byte[] parseKey(InputStream inputFileStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] cryptoKey = new byte[256]; // 2048비트 고정
        
        BufferedInputStream in = new BufferedInputStream(inputFileStream);
        in.read(cryptoKey); /* read crypto key */
        
        return new AsymmetricCipher().decryptKey(cryptoKey);
    }
}
