package com.core.drm.crypto.util;

import com.core.drm.crypto.cipher.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.cipher.asymmetric.AsymmetricKeyManager;
import com.core.drm.crypto.cipher.KeyStorage;
import org.bouncycastle.crypto.engines.AESLightEngine;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import static org.assertj.core.api.Assertions.assertThat;

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
            //FileParser.addKey(outputStream, cryptoKey);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test
    void 암호화_대칭키파일파싱_테스트() {
        byte[] buffer = new byte[1024];
        int len;
        try (InputStream inputStream = new FileInputStream("./encKeyFile.txt");
             OutputStream outputStream = new FileOutputStream("./plainText.txt")
        ) {
            //byte[] key = FileParser.parseKey(inputStream);

            //System.out.println(key.length);
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

    @Test
    void 파일_메타데이터_추가하기() throws IOException {

        //최초 요청시 요청 검사에서 수행할것
        String path = "./fileSample/sample3.xlsx";
        Path file = new File(path).toPath();
        String flag = "true";
        Files.setAttribute(file, "user:encrypt", flag.getBytes());

        assertThat(flag).isEqualTo(new String((byte[]) Files.getAttribute(file, "user:encrypt")));
    }

    @Test
    void 파일_메타데이터_없을때_조회() throws IOException {
        String path = "./fileSample/sample3.xls";
        Path file = new File(path).toPath();

        UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
        assertThat(view.list().contains("encrypt")).isFalse();
    }

    @Test
    void 파일_메타데이터_있을때_조회() throws IOException {
        String path = "./cryptFile/(유통혁신팀) 소매 우수사례 게시판 개편안.xlsx";
        Path file = new File(path).toPath();

        UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);

        System.out.println(view.list().toString());

        assertThat(view.list().contains("encrypt")).isTrue();
    }
}
