package com.core.drm.crypto.service;

import com.core.drm.crypto.cipher.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.cipher.asymmetric.AsymmetricKeyManager;
import com.core.drm.crypto.cipher.KeyStorage;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CipherFileTest {


    @Test
    @DisplayName("A")
    void 파일_암호화() {
        try (
                InputStream inputStream = new FileInputStream("./fileSample/sample2.doc");
                OutputStream fileOutputStream = new FileOutputStream("./fileSample/enc_sample2.doc");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ){
            new DRMCipherService(new AsymmetricCipher(new AsymmetricKeyManager()), new KeyStorage())
                    .encryptFile(inputStream, outputStream, new AESLightEngine());

            fileOutputStream.write(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("B")
    void 파일_복호화() {
        try (
                InputStream inputStream = new FileInputStream("./fileSample/enc_sample3.xlsx");
                OutputStream outputStream = new FileOutputStream("./fileSample/dec_sample3.xlsx")
        ) {
            new DRMCipherService(new AsymmetricCipher(new AsymmetricKeyManager()), new KeyStorage())
                    .decryptFile(inputStream, outputStream, new AESLightEngine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
