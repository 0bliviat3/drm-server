package com.core.drm.crypto.domain.asymmetric;

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

    private byte[] readKey(String type) throws IOException {

        PemReader pemReader = new PemReader(new FileReader(PropertiesUtil.getApplicationProperty(type)));
        PemObject pemObject = pemReader.readPemObject();
        byte[] keyByte = pemObject.getContent();

        pemReader.close();

        return keyByte;
    }

    public PublicKey getPublicKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(readKey("rsa.public.key.path")));
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(readKey("rsa.private.key.path")));
    }

}
