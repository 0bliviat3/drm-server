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
     */
    public static void addKeyFile(OutputStream outputStream, SecretKey key) throws IOException {
        //key 공개키 암호화 처리 -> 클래스분리 고민
        byte[] cryptoKey = cryptKey(key);

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
     * 나머지는 암호화파일 -> 출력
     */
    public static SecretKey parseFile(InputStream inputFileStream, OutputStream cryptFileStream) { //TODO: 구현
        return null;
    }
}
