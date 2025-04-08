package com.core.drm.crypto.domain.cipher;

import com.core.drm.crypto.exception.KeyException;
import org.bouncycastle.crypto.BlockCipher;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

public class KeyStorage {

    public SecretKey generateKey(BlockCipher cipher, int keySize) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(cipher.getAlgorithmName());
            gen.init(keySize);

            return gen.generateKey();
        } catch (InvalidParameterException | NoSuchAlgorithmException e) {
            throw new KeyException("[ERROR] 대칭키 생성 오류", e);
        }
    }

}
