package com.core.drm.crypto.domain.asymmetric;

import org.bouncycastle.crypto.BufferedAsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/*
비대칭키를 사용한 암복호화 클래스
 */
public class AsymmetricCipher {

    public AsymmetricCipher() {
    }

    public byte[] cryptKey(SecretKey key) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedAsymmetricBlockCipher cipher = new BufferedAsymmetricBlockCipher(new RSAEngine());
        RSAPublicKey publicKey = (RSAPublicKey) new AsymmetricKeyManager().getPublicKey();
        byte[] plainKey = key.getEncoded();

        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(
                false, publicKey.getModulus(), publicKey.getPublicExponent());

        cipher.init(true, rsaKeyParameters);
        cipher.processBytes(plainKey, 0, plainKey.length);

        try {
            return cipher.doFinal();
        } catch (InvalidCipherTextException e) {
            throw new IllegalStateException("[ERROR] 대칭키 암호화 실패: " + e.getMessage()); //TODO: 예외처리
        }
    }

    public byte[] decryptKey(byte[] cryptoKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        BufferedAsymmetricBlockCipher cipher = new BufferedAsymmetricBlockCipher(new RSAEngine());
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) new AsymmetricKeyManager().getPrivateKey();

        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(
                true, rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());

        cipher.init(false, rsaKeyParameters);
        cipher.processBytes(cryptoKey, 0, cryptoKey.length);

        try {
            return cipher.doFinal();
        } catch (InvalidCipherTextException e) {
            throw new IllegalStateException("[ERROR] 대칭키 복호화 실패: " + e.getMessage()); //TODO: 예외처리
        }
    }

}
