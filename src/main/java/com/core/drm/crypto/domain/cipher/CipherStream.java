package com.core.drm.crypto.domain.cipher;

import com.core.drm.crypto.exception.CipherException;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * 스트림을 암복호화 처리함
 * 파라미터로 스트림을 전달 받아 암복호화된 스트림의 해제까지 관리
 * 하나의 단일 암복호화만 처리함
 * CipherInputStream, CipherOutputStream 생성,소멸을 관리
 */
public class CipherStream implements Closeable {

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final CipherWrapper cipher;

    public CipherStream(
            final InputStream inputStream,
            final OutputStream outputStream,
            final CipherWrapper cipher) {
        validateCipherStream(inputStream, outputStream, cipher);

        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.cipher = cipher;
    }

    private void validateCipherStream(InputStream inputStream, OutputStream outputStream, CipherWrapper cipher) {
        validateStream(inputStream, outputStream);
        validateCipher(cipher);
    }

    private void validateStream(InputStream inputStream, OutputStream outputStream) {
        if (inputStream == null || outputStream == null) {
            throw new CipherException("[ERROR] 입출력 스트림이 초기화되지 않음");
        }
    }

    private void validateCipher(CipherWrapper cipher) {
        if (!cipher.isInitCipher()) {
            throw new CipherException("[ERROR] cipher 객체가 초기화되지 않음");
        }
    }

    public void encrypt() {
        byte[] buffer = new byte[1024]; //TODO: os레벨에서 설정가능하도록 프로퍼티로 변경
        int readLength;

        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {


            while ((readLength = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, readLength);
            }
            cipherOutputStream.flush();
        } catch (IOException e) {
            throw new CipherException("[ERROR] 파일 암호화 오류", e);
        }

    }

    public void decrypt() {
        byte[] buffer = new byte[1024]; //TODO: os레벨에서 설정가능하도록 프로퍼티로 변경
        int readLength;

        try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher)) {


            while ((readLength = cipherInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }

            outputStream.flush();
        } catch (IOException e) {
            throw new CipherException("[ERROR] 파일 복호화 오류", e);
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }
}
