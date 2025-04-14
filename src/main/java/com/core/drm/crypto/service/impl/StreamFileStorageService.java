package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.domain.CipherFile;
import com.core.drm.crypto.exception.FileException;
import com.core.drm.crypto.service.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class StreamFileStorageService implements FileStorageService {

    @Override
    public <T> T responseFile(CipherFile cipherFile, Class<T> classType) {
        if (classType.equals(InputStream.class)) {
            return classType.cast(cipherFile.getInputStream());
        }
        throw new FileException(
                String.format("[ERROR] 리턴타입 에러: 입력스트림을 기대하나 %s 으로 지정함", classType.getName()));
    }
}
