package com.core.drm.crypto.service;

import com.core.drm.crypto.constant.ProcessState;
import com.core.drm.crypto.domain.entity.CryptoHistory;
import com.core.drm.crypto.domain.entity.CryptoResult;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.repository.CryptoResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoResultService {

    private final CryptoResultRepository cryptoResultRepository;

    public CryptoResult saveCryptoResult(FileRequest fileRequest, ProcessState processState) {
        CryptoResult cryptoResult = CryptoResult.builder()
                .fileRequest(fileRequest)
                .processEndTime(LocalDateTime.now())
                .retryCount(0)
                .processState(processState)
                .build();

        return cryptoResultRepository.save(cryptoResult);
    }

    public void saveCryptoResult(CryptoHistory cryptoHistory) {
        CryptoResult cryptoResult = CryptoResult.builder()
                .fileRequest(cryptoHistory.getFileRequest())
                .processEndTime(cryptoHistory.getProcessTime())
                .retryCount(0)
                .processState(cryptoHistory.getProcessState())
                .build();

        cryptoResultRepository.save(cryptoResult);
    }

}
