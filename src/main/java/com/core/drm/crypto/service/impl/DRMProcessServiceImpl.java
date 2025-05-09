package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.constant.CipherType;
import com.core.drm.crypto.constant.errormessage.CipherExceptionMessage;
import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.service.*;
import com.core.drm.crypto.util.FileUtil;
import com.core.drm.crypto.util.SignValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.core.drm.crypto.constant.CipherType.DECRYPT;
import static com.core.drm.crypto.constant.CipherType.ENCRYPT;
import static com.core.drm.crypto.constant.errormessage.CipherExceptionMessage.FAIL_DECRYPT;
import static com.core.drm.crypto.constant.errormessage.CipherExceptionMessage.FAIL_ENCRYPT;

@Slf4j
@Service
@RequiredArgsConstructor
public class DRMProcessServiceImpl implements DRMProcessService {

    private final DRMCipherService drmCipherService;
    private final FileStorageService fileStorageService;
    private final FileRequestService requestService;
    private final FileTempStorageService fileTempStorageService;


    @Override
    public InputStream encryptFile(MultipartFile file, HttpServletRequest request) {
        FileRequest fileRequest = requestService.saveFileRequest(file, request, ENCRYPT);
        return cryptProcess(fileRequest, drmCipherService::encryptFile);
    }

    @Override
    public InputStream decryptFile(MultipartFile file, HttpServletRequest request) {
        FileRequest fileRequest = requestService.saveFileRequest(file, request, DECRYPT);
        return cryptProcess(fileRequest, drmCipherService::decryptFile);
    }

    private CipherExceptionMessage getErrorMsg(boolean encryptFlag) {
        if (encryptFlag) {
            return FAIL_ENCRYPT;
        }
        return FAIL_DECRYPT;
    }

    private InputStream cryptProcess(
            FileRequest fileRequest,
            TriConsumer<InputStream, OutputStream, BlockCipher> triConsumer) {
        boolean isEncrypt = fileRequest.isEncrypt();
        MultipartFile file = fileRequest.getFile();
        //파일 임시저장
        String savePath = FileUtil.saveTempFile(file, null);
        //임시저장파일 도메인 생성
        TempFile tempFile = new TempFile(savePath);
        fileTempStorageService.saveFileTempStorage(fileRequest, tempFile);
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
            tempFile = new TempFile(savePath);
            fileTempStorageService.saveFileTempStorage(fileRequest, tempFile);
        } catch (IOException e) {
            throw new CipherException(getErrorMsg(isEncrypt), e);
        }
        //처리된 파일 임시파일 도메인으로 래핑
        TempFile cipherFile = new TempFile(savePath);

        return fileStorageService.responseFile(cipherFile, InputStream.class);
    }
}
