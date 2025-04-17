package com.core.drm.crypto.service;

import com.core.drm.crypto.cipher.asymmetric.AsymmetricCipher;
import com.core.drm.crypto.cipher.CipherStream;
import com.core.drm.crypto.cipher.CipherWrapper;
import com.core.drm.crypto.cipher.KeyStorage;
import com.core.drm.crypto.domain.CipherFileHeader;
import com.core.drm.crypto.util.FileParser;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import static com.core.drm.crypto.constant.FileHeaderKey.*;

@Service
public class DRMCipherService {

    private final AsymmetricCipher asymmetricCipher;
    private final KeyStorage keyStorage;

    @Autowired
    public DRMCipherService(final AsymmetricCipher asymmetricCipher, final KeyStorage keyStorage) {
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

        CipherFileHeader header = FileParser.generateHeader(null, asymmetricCipher.cryptKey(key), iv);

        FileParser.addHeader(outputStream, header);

        CipherConfig cipherConfig = new CipherConfig(true, cipher, key.getEncoded(), iv);
        CipherStream cipherStream = initCipherStream(cipherConfig, inputStream, outputStream);
        cipherStream.encrypt();
    }

    public void decryptFile(InputStream inputStream, OutputStream outputStream, BlockCipher cipher) {
        CipherFileHeader header = FileParser.parseHeader(inputStream);

        CipherConfig cipherConfig = new CipherConfig(
                false,
                cipher,
                asymmetricCipher.decryptKey(header.getByte(KEY)),
                header.getByte(IV)
        );
        CipherStream cipherStream = initCipherStream(cipherConfig, inputStream, outputStream);

        cipherStream.decrypt();
    }

    private record CipherConfig(boolean mode, BlockCipher cipher, byte[] key, byte[] iv) {
    }

}
