package com.core.drm.crypto.cipher.asymmetric;

import com.core.drm.crypto.exception.CipherException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.BufferedAsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/*
비대칭키를 사용한 암복호화 클래스
 */
@Slf4j
@Component
public class AsymmetricCipher {

    private final AsymmetricKeyManager asymmetricKeyManager;

    @Autowired
    public AsymmetricCipher(final AsymmetricKeyManager asymmetricKeyManager) {
        this.asymmetricKeyManager = asymmetricKeyManager;
    }

    public byte[] cryptKey(SecretKey key) {
        log.info("encrypt key");
        BufferedAsymmetricBlockCipher cipher = new BufferedAsymmetricBlockCipher(new RSAEngine());
        RSAPublicKey publicKey = (RSAPublicKey) asymmetricKeyManager.getPublicKey();
        byte[] plainKey = key.getEncoded();

        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(
                false, publicKey.getModulus(), publicKey.getPublicExponent());

        cipher.init(true, rsaKeyParameters);
        cipher.processBytes(plainKey, 0, plainKey.length);

        try {
            return cipher.doFinal();
        } catch (InvalidCipherTextException e) {
            log.error("cipher exception: {}", e.getMessage());
            throw new CipherException("[ERROR] 대칭키 비대칭 암호화 실패", e);
        }
    }

    public byte[] decryptKey(byte[] cryptoKey) {
        log.info("decrypt key");
        BufferedAsymmetricBlockCipher cipher = new BufferedAsymmetricBlockCipher(new RSAEngine());
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) asymmetricKeyManager.getPrivateKey();

        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(
                true, rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());

        cipher.init(false, rsaKeyParameters);
        cipher.processBytes(cryptoKey, 0, cryptoKey.length);

        try {
            return cipher.doFinal();
        } catch (InvalidCipherTextException e) {
            throw new CipherException("[ERROR] 대칭키 비대칭 복호화 실패", e);
        }
    }

}
