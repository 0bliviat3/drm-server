package com.core.drm.crypto.domain;

import org.bouncycastle.crypto.BlockCipher;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyStorage {

    public SecretKey generateKey(BlockCipher cipher, int keySize) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(cipher.getAlgorithmName());
            gen.init(keySize);

            return gen.generateKey();
        } catch (NoSuchAlgorithmException | InvalidParameterException e) {
            //TODO: key size에 대해 에러발생할 가능성 있음. 에러 래핑할것
            throw new IllegalStateException("[ERROR] 키생성 오류: " + e.getMessage());
        }
    }

    /*
    공개키, 비공개키 모두 OS 레벨에서 관리하므로 스트림처리
     */
    public PublicKey getPublicKey()  {
        return null;
    }

    public PrivateKey getPrivateKey() {
        return null;
    }


}
