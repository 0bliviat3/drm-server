package com.core.drm.crypto;

import com.core.drm.crypto.cipher.CipherWrapper;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PasswordUtil;

import javax.crypto.*;
import javax.security.auth.DestroyFailedException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class CryptoTest {


    private String getRes(byte[] targetData, GCMBlockCipher cipher, Function<byte[], String> function) {
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
    void 비밀번호_해싱() throws NoSuchAlgorithmException {
        String password = "qwer1234";
        String id = "admin";
        String hashPass = PasswordUtil.encodeScramSha256(password.toCharArray(), 3, id.getBytes());
//        System.out.println(hashPass);
        String dbHash = "SCRAM-SHA-256$3:YWRtaW4=$hdhaD6AI5F5XMca9RHixy5cNEQ/QihzNzErdgSALxQQ=:WtxYkG/5GukekIZBGPs+GI64SvPcAVyZzZwWG+ty06M=";
        assertThat(hashPass).isEqualTo(dbHash);
    }

    @Test
    void 암호화_사용방법() {
        String key = "1234567891234567";
        String plainText = "abcde";

        GCMBlockCipher cipher = new CipherWrapper(new AESLightEngine());
        cipher.init(true, new KeyParameter(key.getBytes()));

        String res = getRes(plainText.getBytes(), cipher, this::encodeToString);

        System.out.println(res);
    }

    @Test
    void 복호화_사용방법() {
        String key = "1234567891234567";
        String cipherText = "bkFnddPWV9J6QFsjupZwvg==";

        GCMBlockCipher cipher = new CipherWrapper(new AESLightEngine());
        cipher.init(false, new KeyParameter(key.getBytes()));

        String res = getRes(Base64.getDecoder().decode(cipherText), cipher, String::new);

        System.out.println(res.trim());
    }

    @Test
    void 대칭키_생성방법() throws NoSuchAlgorithmException, DestroyFailedException {

        String plainText = "abcde";

        BlockCipher cipher = new AESLightEngine();
        KeyGenerator gen = KeyGenerator.getInstance(cipher.getAlgorithmName());
        gen.init(128);
        SecretKey key = gen.generateKey();

        System.out.println("key: " + encodeToString(key.getEncoded()));

        GCMBlockCipher cipherWrapper = new CipherWrapper(cipher);
        cipherWrapper.init(true, new KeyParameter(key.getEncoded()));

        String res = getRes(plainText.getBytes(), cipherWrapper, this::encodeToString);

        System.out.println("암호화: " + res);
        cipherWrapper.init(false, new KeyParameter(key.getEncoded()));

        String resPlain = getRes(Base64.getDecoder().decode(res), cipherWrapper, String::new);

        System.out.println("복호화: " + resPlain.trim());
    }

    @Test
    void 키_최대_사이즈_확인() throws NoSuchAlgorithmException, NoSuchPaddingException {
        BlockCipher blockCipher = new SEEDEngine();
        int size = Cipher.getMaxAllowedKeyLength(blockCipher.getAlgorithmName());

        System.out.println(size);

        System.out.println(1 << 16);
    }

}
