package com.core.drm.crypto.repository;

import com.core.drm.admin.dto.CryptoRequestDTO;
import com.core.drm.crypto.domain.entity.FileRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FileRequestRepository extends JpaRepository<FileRequest, UUID> {

    @Query(value = """
            SELECT
            	count(A.request_id) as count, A.request_type as type
            FROM t_file_request A
            WHERE 1 = 1
            AND TO_CHAR(A.request_time , 'YYYY-MM-DD') = TO_CHAR(NOW(), 'YYYY-MM-DD')
            GROUP BY A.request_type
            """, nativeQuery = true)
    List<CryptoRequestDTO> countRequestByTypeToday();

}
