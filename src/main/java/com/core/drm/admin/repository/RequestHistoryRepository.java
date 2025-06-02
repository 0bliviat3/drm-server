package com.core.drm.admin.repository;

import com.core.drm.admin.domain.RequestHistory;
import com.core.drm.admin.dto.RequestCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RequestHistoryRepository extends JpaRepository<RequestHistory, UUID> {

    @Query(value = """
            SELECT
            	TO_CHAR(request_time, 'YYYY-MM-DD') AS date, COUNT(request_time) AS count
            FROM t_request_history
            WHERE 1 = 1
            AND request_time > NOW() - INTERVAL '7 DAYS'
            GROUP BY TO_CHAR(request_time, 'YYYY-MM-DD')
            """, nativeQuery = true)
    List<RequestCountDTO> countRequestHistoryWeekly();
}
