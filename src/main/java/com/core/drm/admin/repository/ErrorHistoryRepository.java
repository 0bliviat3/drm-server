package com.core.drm.admin.repository;

import com.core.drm.admin.domain.ErrorHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ErrorHistoryRepository extends JpaRepository<ErrorHistory, UUID> {
}
