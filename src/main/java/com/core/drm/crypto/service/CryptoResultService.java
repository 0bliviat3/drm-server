package com.core.drm.crypto.service;

import com.core.drm.base.batch.exception.BatchException;
import com.core.drm.crypto.constant.ProcessState;
import com.core.drm.crypto.domain.entity.CryptoHistory;
import com.core.drm.crypto.domain.entity.CryptoResult;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.repository.CryptoResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.core.drm.base.batch.constant.errormessage.BatchExceptionMessage.FAIL_BULK_PROCESS;
import static com.core.drm.crypto.constant.FileState.EXIST;

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
                .fileState(EXIST)
                .build();

        return cryptoResultRepository.save(cryptoResult);
    }

    public void saveCryptoResult(CryptoHistory cryptoHistory) {
        CryptoResult cryptoResult = CryptoResult.builder()
                .fileRequest(cryptoHistory.getFileRequest())
                .processEndTime(cryptoHistory.getProcessTime())
                .retryCount(0)
                .processState(cryptoHistory.getProcessState())
                .fileState(EXIST)
                .build();

        cryptoResultRepository.save(cryptoResult);
    }

    public void updateFileStateToRemoved(List<UUID> requestIds) {
        try {
            cryptoResultRepository.bulkUpdateStateToRemoved(requestIds);
        } catch (Exception e) {
            throw new BatchException(FAIL_BULK_PROCESS, e);
        }
    }

    public Page<CryptoResult> findAll(Pageable pageable) {
        return cryptoResultRepository.findAll(pageable);
    }

}
