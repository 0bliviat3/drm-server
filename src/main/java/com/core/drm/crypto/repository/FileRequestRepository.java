package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.FileRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRequestRepository extends JpaRepository<FileRequest, UUID> {
}
