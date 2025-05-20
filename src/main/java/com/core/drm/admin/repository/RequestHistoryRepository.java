package com.core.drm.admin.repository;

import com.core.drm.admin.domain.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestHistoryRepository extends JpaRepository<RequestHistory, UUID> {
}
