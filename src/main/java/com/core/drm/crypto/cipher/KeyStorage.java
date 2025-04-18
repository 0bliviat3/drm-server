package com.core.drm.crypto.cipher;

import com.core.drm.crypto.exception.KeyException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.BlockCipher;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import static com.core.drm.crypto.constant.errormessage.KeyExceptionMessage.FAIL_GENERATE_KEY;

@Slf4j
@Component
public class KeyStorage {

    public SecretKey generateKey(BlockCipher cipher, int keySize) {
        try {
            String algorithm = cipher.getAlgorithmName();
            KeyGenerator gen = KeyGenerator.getInstance(algorithm);
            gen.init(keySize);
            log.info("Generator {} key", algorithm);
            return gen.generateKey();
        } catch (InvalidParameterException | NoSuchAlgorithmException e) {
            throw new KeyException(FAIL_GENERATE_KEY, e);
        }
    }

}
