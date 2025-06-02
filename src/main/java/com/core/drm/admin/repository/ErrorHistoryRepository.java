package com.core.drm.admin.repository;

import com.core.drm.admin.domain.ErrorHistory;
import com.core.drm.admin.dto.ErrorCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ErrorHistoryRepository extends JpaRepository<ErrorHistory, UUID> {

    @Query(value = """
            SELECT
            	event_time::DATE AS date, COUNT(event_time)
            FROM t_error_history
            WHERE 1 = 1
            AND event_time::DATE > NOW() - INTERVAL '7 DAYS'
            GROUP BY event_time::DATE
            ORDER BY date
            """, nativeQuery = true)
    List<ErrorCountDTO> countErrorHistoryWeekly();

}
