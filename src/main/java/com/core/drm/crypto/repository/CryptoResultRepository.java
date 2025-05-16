package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.CryptoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CryptoResultRepository extends JpaRepository<CryptoResult, UUID> {

    @Modifying
    @Query("""
            UPDATE t_crypto_result
            SET file_state = 'REMOVED'
            WHERE 1 = 1
            AND request_id IN :requestIds
            """)
    void bulkUpdateStateToRemoved(@Param("requestIds") List<UUID> requestIds);
}
