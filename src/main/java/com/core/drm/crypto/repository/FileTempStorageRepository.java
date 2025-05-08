package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.FileTempStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileTempStorageRepository extends JpaRepository<FileTempStorage, UUID> {
}
