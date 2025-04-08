package com.core.drm.crypto.domain.asymmetric;

import com.core.drm.crypto.exception.KeyException;
import com.core.drm.crypto.util.PropertiesUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*
비대칭 키를 관리하는 클래스
공개키 로드, 비공개키 로드
 */
public class AsymmetricKeyManager {

    public AsymmetricKeyManager() {
    }

    private byte[] readKey(String type) {
        String path = PropertiesUtil.getApplicationProperty(type);
        try (PemReader pemReader = new PemReader(new FileReader(path))) {
            PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();
        } catch (IOException e) {
            throw new KeyException("[ERROR] pem 파일 읽기 실패!, 파일경로: " + path, e);
        }
    }

    public PublicKey getPublicKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(readKey("rsa.public.key.path")));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException("[ERROR] 공개키 발행 실패", e);
        }
    }

    public PrivateKey getPrivateKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(readKey("rsa.private.key.path")));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KeyException("[ERROR] 개인키 발생 실패", e);
        }
    }

}
