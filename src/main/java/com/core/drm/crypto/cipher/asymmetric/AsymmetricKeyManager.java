package com.core.drm.crypto.cipher.asymmetric;

import com.core.drm.crypto.exception.KeyException;
import com.core.drm.crypto.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.math.ec.rfc8032.Ed448;
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

    private static final String ASYMMETRIC_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY_PROPERTY = "rsa.public.key.path";
    private static final String PRIVATE_KEY_PROPERTY = "rsa.private.key.path";

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
            KeyFactory keyFactory = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(readKey(PUBLIC_KEY_PROPERTY)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException(FAIL_GENERATE_PUBLIC_KEY, e);
        }
    }

    public PrivateKey getPrivateKey() {
        log.info("get private key");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(readKey(PRIVATE_KEY_PROPERTY)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException(FAIL_GENERATE_PRIVATE_KEY, e);
        }
    }

}
