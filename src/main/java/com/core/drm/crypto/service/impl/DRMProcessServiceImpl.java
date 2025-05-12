package com.core.drm.crypto.service.impl;

import com.core.drm.crypto.constant.errormessage.CipherExceptionMessage;
import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.service.*;
import com.core.drm.crypto.util.FileUtil;
import com.core.drm.crypto.util.SignValidator;
import com.core.drm.crypto.util.ThreadLocalMapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.TriConsumer;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.core.drm.crypto.constant.CipherType.DECRYPT;
import static com.core.drm.crypto.constant.CipherType.ENCRYPT;
import static com.core.drm.crypto.constant.ProcessState.*;
import static com.core.drm.crypto.constant.ThreadKey.FILE_REQUEST;
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
    private final CryptoHistoryService cryptoHistoryService;


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

    private TempFile prepareTempFile(FileRequest fileRequest) {
        //요청 id 저장
        ThreadLocalMapUtil.put(FILE_REQUEST, fileRequest);
        MultipartFile file = fileRequest.getFile();
        //파일 임시저장
        String savePath = FileUtil.saveTempFile(file, null);
        //임시저장파일 도메인 생성
        TempFile tempFile =  new TempFile(savePath);
        fileTempStorageService.saveFileTempStorage(fileRequest, tempFile);
        return tempFile;
    }

    private TempFile processFile(
            FileRequest fileRequest,
            TempFile tempFile,
            TriConsumer<InputStream, OutputStream, BlockCipher> triConsumer) {
        //결과 출력 스트림 생성
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                InputStream inputStream = tempFile.getInputStream()
        ) {
            //암복호화처리
            triConsumer.accept(inputStream, outputStream, new AESLightEngine());
            byte[] outputByte = outputStream.toByteArray();
            //처리된 파일 임시저장
            String savePath = FileUtil.saveTempFile(outputByte, fileRequest.getFileName(), null);
            TempFile cryptTempFile = new TempFile(savePath);
            fileTempStorageService.saveFileTempStorage(fileRequest, cryptTempFile);
            return cryptTempFile;
        } catch (Exception e) {
            throw new CipherException(getErrorMsg(fileRequest.isEncrypt()), e);
        }
    }

    private InputStream cryptProcess(
            FileRequest fileRequest,
            TriConsumer<InputStream, OutputStream, BlockCipher> triConsumer) {
        //히스토리 [진행]저장
        cryptoHistoryService.saveCryptoHistory(fileRequest, IN_PROCESS);
        TempFile tempFile = prepareTempFile(fileRequest);
        //임시파일 암복호화 정합성 검사
        SignValidator.validateSign(tempFile, fileRequest.isEncrypt());
        //처리된 파일 임시파일 도메인으로 래핑
        TempFile cipherFile = processFile(fileRequest, tempFile, triConsumer);

        //프로세스 [성공]저장
        cryptoHistoryService.saveCryptoHistory(fileRequest, SUCCESS);
        return fileStorageService.responseFile(cipherFile, InputStream.class);
    }
}
