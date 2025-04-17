package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.exception.FileException;
import com.core.drm.crypto.service.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static com.core.drm.crypto.constant.errormessage.FileExceptionMessage.WRONG_RETURN_TYPE;

@Service
public class StreamFileStorageService implements FileStorageService {

    @Override
    public <T> T responseFile(TempFile cipherFile, Class<T> classType) {
        if (classType.equals(InputStream.class)) {
            return classType.cast(cipherFile.getInputStream());
        }
        throw new FileException(WRONG_RETURN_TYPE, classType.getName());
    }
}
