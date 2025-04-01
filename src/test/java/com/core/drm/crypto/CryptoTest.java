package com.core.drm.crypto;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Function;

public class CryptoTest {


    private String getRes(byte[] targetData, BufferedBlockCipher cipher, Function<byte[], String> function) {
        byte[] output = new byte[cipher.getOutputSize(targetData.length)];
        int code = cipher.processBytes(targetData, 0, targetData.length, output, 0);

        try {
            cipher.doFinal(output, code);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }

        return function.apply(output);
    }

    private String encodeToString(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    @Test
    void 암호화_사용방법() {
        String key = "1234567891234567";
        String plainText = "abcde";

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESLightEngine());
        cipher.init(true, new KeyParameter(key.getBytes()));

        String res = getRes(plainText.getBytes(), cipher, this::encodeToString);

        System.out.println(res);
    }

    @Test
    void 복호화_사용방법() {
        String key = "1234567891234567";
        String cipherText = "bkFnddPWV9J6QFsjupZwvg==";

        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESLightEngine());
        cipher.init(false, new KeyParameter(key.getBytes()));

        String res = getRes(Base64.getDecoder().decode(cipherText), cipher, String::new);

        System.out.println(res.trim());
    }

}
