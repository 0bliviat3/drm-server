package com.core.drm.crypto.config;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKey {

    private KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); //TODO: 키크기, 알고리즘 상수 설정

        return keyGen.generateKeyPair();
    }

    private void writeKey(Key key, String description, String fileName) throws IOException {
        Pem pemFile = new Pem(key, description);
        pemFile.write(fileName);
    }

    public void generate() throws NoSuchAlgorithmException, IOException {
        KeyPair keys = generateKey();
        writeKey(keys.getPrivate(), "RSA PRIVATE KEY", "./private.pem");
        writeKey(keys.getPublic(), "RSA PUBLIC KEY", "./public.pem");
    }


}
