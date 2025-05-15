package com.core.drm.crypto.repository;

import com.core.drm.crypto.domain.entity.FileTempStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FileTempStorageRepository extends JpaRepository<FileTempStorage, UUID> {

    @Query(value = """
            SELECT
            	A
            FROM t_file_temp_storage A
            LEFT JOIN t_crypto_result B
            ON A.request_id = B.request_id
            WHERE 1 = 1
            AND B.process_state = 'SUCCESS'
            AND B.file_state = 'EXIST'
            AND TO_CHAR(A.save_time, 'YYYY-MM-DD') = TO_CHAR(NOW() - '1 days'::INTERVAL, 'YYYY-MM-DD')
            """)
    List<FileTempStorage> findSavedYesterdayWithSuccessAndExistResult();
}
