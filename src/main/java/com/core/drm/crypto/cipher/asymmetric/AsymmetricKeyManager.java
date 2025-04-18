package com.core.drm.crypto.cipher.asymmetric;

import com.core.drm.crypto.exception.KeyException;
import com.core.drm.crypto.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.core.drm.crypto.constant.errormessage.KeyExceptionMessage.*;

/*
비대칭 키를 관리하는 클래스
공개키 로드, 비공개키 로드
 */
@Slf4j
@Component
public class AsymmetricKeyManager {

    public AsymmetricKeyManager() {
    }

    private byte[] readKey(String type) {
        log.info("read key");
        String path = PropertiesUtil.getApplicationProperty(type);
        try (PemReader pemReader = new PemReader(new FileReader(path))) {
            PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        } catch (IOException e) {
            throw new KeyException(FAIL_PEM_LOAD, e, path);
        }
    }

    public PublicKey getPublicKey() {
        log.info("get public key");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(readKey("rsa.public.key.path")));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException(FAIL_GENERATE_PUBLIC_KEY, e);
        }
    }

    public PrivateKey getPrivateKey() {
        log.info("get private key");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(readKey("rsa.private.key.path")));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException(FAIL_GENERATE_PRIVATE_KEY, e);
        }
    }

}
