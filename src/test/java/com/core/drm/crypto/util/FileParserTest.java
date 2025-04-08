package com.core.drm.crypto.util;

import com.core.drm.crypto.domain.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.domain.asymmetric.AsymmetricKeyManager;
import com.core.drm.crypto.domain.cipher.KeyStorage;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileParserTest {

    @Test
    void 암호화_대칭키파일생성_테스트() {
        //암호화 대칭키생성
        SecretKey key = new KeyStorage().generateKey(new AESLightEngine(), 128);
        AsymmetricKeyManager keyManager = new AsymmetricKeyManager();
        AsymmetricCipher asymmetricCipher = new AsymmetricCipher(keyManager);
        byte[]cryptoKey = asymmetricCipher.cryptKey(key);

        //구분용 평문
        String plainText = "plainText";
        byte[] plainByte = plainText.getBytes();

        byte[] buffer = new byte[1024];
        int len;
        try (
                OutputStream outputStream = new FileOutputStream("./encKeyFile.txt");
                InputStream inputStream = new ByteArrayInputStream(plainByte)
        ) {
            FileParser.addKey(outputStream, cryptoKey);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void 암호화_대칭키파일파싱_테스트() {
        byte[] buffer = new byte[1024];
        int len;
        try (InputStream inputStream = new FileInputStream("./encKeyFile.txt");
             OutputStream outputStream = new FileOutputStream("./plainText.txt")
        ) {
            byte[] key = FileParser.parseKey(inputStream);

            System.out.println(key.length);
            try (BufferedInputStream remainingStream = new BufferedInputStream(inputStream)) {
                while ((len = remainingStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len); // 평문 내용 파일에 쓰기
                }
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
