package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.CryptoHistory;
import com.core.drm.crypto.domain.entity.FileRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CryptoHistoryRepository extends JpaRepository<CryptoHistory, UUID> {
    Optional<CryptoHistory> findFirstByFileRequestOrderByProcessTimeDesc(FileRequest fileRequest);
}
