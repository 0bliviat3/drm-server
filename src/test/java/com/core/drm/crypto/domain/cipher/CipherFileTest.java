package com.core.drm.crypto.domain.cipher;

import com.core.drm.crypto.domain.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.domain.asymmetric.AsymmetricKeyManager;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CipherFileTest {


    @Test
    @DisplayName("A")
    void 파일_암호화() {
        try (
                InputStream inputStream = new FileInputStream("./fileSample/sample2.doc");
                OutputStream outputStream = new FileOutputStream("./fileSample/enc_sample2.doc")
        ){
            new CipherFile(new AsymmetricCipher(new AsymmetricKeyManager()), new KeyStorage())
                    .encryptFile(inputStream, outputStream, new AESLightEngine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("B")
    void 파일_복호화() {
        try (
                InputStream inputStream = new FileInputStream("./fileSample/enc_sample2.doc");
                OutputStream outputStream = new FileOutputStream("./fileSample/dec_sample2.doc")
        ){
            new CipherFile(new AsymmetricCipher(new AsymmetricKeyManager()), new KeyStorage())
                    .decryptFile(inputStream, outputStream, new AESLightEngine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
