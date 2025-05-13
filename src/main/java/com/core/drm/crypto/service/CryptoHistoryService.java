package com.core.drm.crypto.service;

import com.core.drm.crypto.constant.ProcessState;
import com.core.drm.crypto.domain.entity.CryptoHistory;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.repository.CryptoHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoHistoryService {

    private final CryptoHistoryRepository cryptoHistoryRepository;

    public CryptoHistory saveCryptoHistory(FileRequest fileRequest, ProcessState state) {
        CryptoHistory cryptoHistory = CryptoHistory.builder()
                .fileRequest(fileRequest)
                .processState(state)
                .processTime(LocalDateTime.now())
                .build();

        return cryptoHistoryRepository.save(cryptoHistory);
    }

    public CryptoHistory findByRequestId(FileRequest fileRequest) {
        return cryptoHistoryRepository
                .findFirstByFileRequestOrderByProcessTimeDesc(fileRequest)
                .orElseThrow(EntityNotFoundException::new);
    }

}
