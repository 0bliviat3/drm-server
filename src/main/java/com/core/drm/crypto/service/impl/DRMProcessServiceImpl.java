package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.constant.errormessage.CipherExceptionMessage;
import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.service.DRMCipherService;
import com.core.drm.crypto.service.DRMProcessService;
import com.core.drm.crypto.service.FileStorageService;
import com.core.drm.crypto.util.FileUtil;
import com.core.drm.crypto.util.SignValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.core.drm.crypto.constant.errormessage.CipherExceptionMessage.FAIL_DECRYPT;
import static com.core.drm.crypto.constant.errormessage.CipherExceptionMessage.FAIL_ENCRYPT;

@Slf4j
@Service
public class DRMProcessServiceImpl implements DRMProcessService {

    private final DRMCipherService drmCipherService;
    private final FileStorageService fileStorageService;

    @Autowired
    public DRMProcessServiceImpl(final DRMCipherService drmCipherService, final FileStorageService fileStorageService) {
        this.drmCipherService = drmCipherService;
        this.fileStorageService = fileStorageService;
    }


    @Override
    public InputStream encryptFile(MultipartFile file) {
        return cryptProcess(file, true, FAIL_ENCRYPT, drmCipherService::encryptFile);
    }

    @Override
    public InputStream decryptFile(MultipartFile file) {
        return cryptProcess(file, false, FAIL_DECRYPT, drmCipherService::decryptFile);
    }

    private InputStream cryptProcess(
            MultipartFile file,
            boolean isEncrypt,
            CipherExceptionMessage errMessage,
            TriConsumer<InputStream, OutputStream, BlockCipher> triConsumer) {
        //파일 임시저장
        String savePath = FileUtil.saveTempFile(file, null);
        //TODO: DB에도 경로, 원본이름 저장
        //임시저장파일 도메인 생성
        TempFile tempFile = new TempFile(savePath);
        //임시파일 암복호화 정합성 검사
        SignValidator.validateSign(tempFile, isEncrypt);
        //결과 출력 스트림 생성
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                InputStream inputStream = tempFile.getInputStream()
        ) {
            //암호화처리
            triConsumer.accept(inputStream, outputStream, new AESLightEngine());
            byte[] outputByte = outputStream.toByteArray();
            //처리된 파일 임시저장
            savePath = FileUtil.saveTempFile(outputByte, file.getOriginalFilename(), null);
            //TODO: DB에도 경로 저장
        } catch (IOException e) {
            throw new CipherException(errMessage, e);
        }
        //처리된 파일 임시파일 도메인으로 래핑
        TempFile cipherFile2 = new TempFile(savePath);

        return fileStorageService.responseFile(cipherFile2, InputStream.class);
    }
}
