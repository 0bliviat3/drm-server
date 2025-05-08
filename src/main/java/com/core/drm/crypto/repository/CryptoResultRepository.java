package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.CryptoResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CryptoResultRepository extends JpaRepository<CryptoResult, UUID> {
}
