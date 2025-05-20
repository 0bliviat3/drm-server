package com.core.drm.crypto.config;

import com.core.drm.crypto.util.PropertiesUtil;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKey {

    private static final String RSA = "RSA";
    private static final int KEY_SIZE = 2048;

    private KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(KEY_SIZE);
        return keyGen.generateKeyPair();
    }

    private void writeKey(Key key, String description, String fileName) throws IOException {
        Pem pemFile = new Pem(key, description);
        pemFile.write(fileName);
    }

    public void generate() throws NoSuchAlgorithmException, IOException {
        KeyPair keys = generateKey();
        String privateKeyPath = PropertiesUtil.getApplicationProperty("rsa.private.key.path");
        String publicKeyPath = PropertiesUtil.getApplicationProperty("rsa.public.key.path");
        writeKey(keys.getPrivate(), "RSA PRIVATE KEY", privateKeyPath);
        writeKey(keys.getPublic(), "RSA PUBLIC KEY", publicKeyPath);
    }


}
