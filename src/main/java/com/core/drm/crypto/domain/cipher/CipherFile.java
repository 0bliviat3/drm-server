package com.core.drm.crypto.domain.cipher;

import com.core.drm.crypto.domain.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.util.FileParser;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class CipherFile {

    private final AsymmetricCipher asymmetricCipher;
    private final KeyStorage keyStorage;

    public CipherFile(final AsymmetricCipher asymmetricCipher, final KeyStorage keyStorage) {
        this.asymmetricCipher = asymmetricCipher;
        this.keyStorage = keyStorage;
    }

    private byte[] generateIV() {
        byte[] iv = new byte[12]; //TODO: 96-bit -> AES-GCM
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        return iv;
    }

    private CipherStream initCipherStream(
            CipherConfig cipherConfig, InputStream inputStream, OutputStream outputStream) {
        CipherWrapper wrapper = new CipherWrapper(cipherConfig.cipher);
        CipherParameters params = new AEADParameters(new KeyParameter(cipherConfig.key), 128, cipherConfig.iv); // MAC 표준 128bit
        wrapper.init(cipherConfig.mode, params);
        return new CipherStream(inputStream, outputStream, wrapper);
    }

    public void encryptFile(InputStream inputStream, OutputStream outputStream, BlockCipher cipher) {
        SecretKey key = keyStorage.generateKey(cipher, 128); //TODO: AES-128사용
        byte[] iv = generateIV();

        CipherConfig cipherConfig = new CipherConfig(true, cipher, key.getEncoded(), iv);
        CipherStream cipherStream = initCipherStream(cipherConfig, inputStream, outputStream);

        FileParser.addKey(outputStream, asymmetricCipher.cryptKey(key));
        FileParser.addIV(outputStream, iv);
        FileParser.validateOutputStreamPointer(outputStream, 256 + 12);
        cipherStream.encrypt();
    }

    public void decryptFile(InputStream inputStream, OutputStream outputStream, BlockCipher cipher) {
        byte[] key = asymmetricCipher.decryptKey(FileParser.parseKey(inputStream));
        byte[] iv = FileParser.parseIV(inputStream);

        CipherConfig cipherConfig = new CipherConfig(false, cipher, key, iv);
        CipherStream cipherStream = initCipherStream(cipherConfig, inputStream, outputStream);

        cipherStream.decrypt();
    }

    private record CipherConfig(boolean mode, BlockCipher cipher, byte[] key, byte[] iv) {
    }

}
