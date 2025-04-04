package com.core.drm.crypto.util;

import com.core.drm.crypto.domain.KeyStorage;
import org.bouncycastle.crypto.BufferedAsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.SecretKey;
import java.io.*;

public class FileParser {

    private FileParser() {
    }

    private static byte[] cryptKey(SecretKey key) {
        BufferedAsymmetricBlockCipher cipher = new BufferedAsymmetricBlockCipher(new RSAEngine());
        byte[] publicKey = new KeyStorage().getPublicKey().getEncoded();
        byte[] plainKey = key.getEncoded();
        cipher.init(true, new KeyParameter(publicKey));
        cipher.processBytes(plainKey, 0, plainKey.length);

        try {
            return cipher.doFinal();
        } catch (InvalidCipherTextException e) {
            throw new IllegalStateException("[ERROR] 대칭키 암호화 실패: " + e.getMessage()); //TODO: 예외처리
        }
    }

    private static SecretKey decryptKey(byte[] cryptoKey) { //TODO: 구현
        return null;
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
