package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.CryptoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CryptoHistoryRepository extends JpaRepository<CryptoHistory, UUID> {
}
