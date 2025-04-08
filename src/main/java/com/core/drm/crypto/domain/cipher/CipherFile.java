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

    public void encryptFile(InputStream inputStream, OutputStream outputStream, BlockCipher cipher) {
        SecretKey key = keyStorage.generateKey(cipher, 128); //TODO: AES-128사용
        byte[] iv = generateIV();

        CipherWrapper wrapper = new CipherWrapper(cipher);
        CipherParameters params = new AEADParameters(new KeyParameter(key.getEncoded()), 128, iv);
        wrapper.init(true, params);
        CipherStream cipherStream = new CipherStream(inputStream, outputStream, wrapper);

        FileParser.addKey(outputStream, asymmetricCipher.cryptKey(key));
        FileParser.addIV(outputStream, iv);
        cipherStream.encrypt();
    }

    public void decryptFile(InputStream inputStream, OutputStream outputStream, BlockCipher cipher) {
        byte[] key = asymmetricCipher.decryptKey(FileParser.parseKey(inputStream));
        byte[] iv = FileParser.parseIV(inputStream);

        CipherWrapper wrapper = new CipherWrapper(cipher);
        CipherParameters params = new AEADParameters(new KeyParameter(key), 128, iv);
        wrapper.init(false, params);
        CipherStream cipherStream = new CipherStream(inputStream, outputStream, wrapper);

        cipherStream.decrypt();
    }

}
