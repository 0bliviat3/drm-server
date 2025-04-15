package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.domain.CipherFile;
import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.service.DRMCipherService;
import com.core.drm.crypto.service.DRMProcessService;
import com.core.drm.crypto.service.FileStorageService;
import com.core.drm.crypto.util.FileUtil;
import org.apache.logging.log4j.util.TriConsumer;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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
        return cryptProcess(file, "true", "[ERROR] 암호화 오류", drmCipherService::encryptFile);
    }

    @Override
    public InputStream decryptFile(MultipartFile file) {
        return cryptProcess(file, "false", "[ERROR] 복호화 오류", drmCipherService::decryptFile);
    }

    private InputStream cryptProcess(
            MultipartFile file,
            String mode,
            String errMessage,
            TriConsumer<InputStream, OutputStream, BlockCipher> triConsumer) {
        //파일 임시저장
        String savePath = FileUtil.saveTempFile(file, null);
        //TODO: DB에도 경로 저장
        //도메인 생성
        CipherFile cipherFile = new CipherFile(savePath, mode);
        //결과 출력 스트림 생성
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                InputStream inputStream = cipherFile.getInputStream()
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
        //메타데이터 설정
        CipherFile cipherFile2 = new CipherFile(savePath, mode);
        cipherFile2.setCryptoFlag();

        return fileStorageService.responseFile(cipherFile2, InputStream.class);
    }
}
