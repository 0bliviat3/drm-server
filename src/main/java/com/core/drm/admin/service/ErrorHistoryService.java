package com.core.drm.admin.service;

import com.core.drm.admin.domain.ErrorHistory;
import com.core.drm.admin.dto.ErrorCountDTO;
import com.core.drm.admin.dto.ErrorHistoryDTO;
import com.core.drm.admin.repository.ErrorHistoryRepository;
import com.core.drm.base.util.StringUtils;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ErrorHistoryService {

    private static final int ERROR_LIMIT = 2500;
    private final ErrorHistoryRepository errorHistoryRepository;

    public ErrorHistoryDTO saveException(Exception exception, String eventTime, ResponseMessage responseMessage) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        String stackTrace = StringUtils.limit(sw.toString(), ERROR_LIMIT);
        ErrorHistory errorHistory = ErrorHistory.builder()
                .errorCode(responseMessage.getCode())
                .eventTime(eventTime)
                .returnMessage(responseMessage.getMessage())
                .errorMessage(exception.getMessage())
                .stackTrace(stackTrace)
                .build();

        return errorHistoryRepository.save(errorHistory).toDTO();
    }

    public ErrorHistoryDTO findById(UUID errorId) {
        return errorHistoryRepository.findById(errorId)
                .orElseThrow(EntityNotFoundException::new)
                .toDTO();
    }

    public Page<ErrorHistoryDTO> findAll(Pageable pageable) {
        return errorHistoryRepository.findAll(pageable)
                .map(ErrorHistory::toDTO);
    }

    public List<ErrorCountDTO> countErrorHistoryWeekly() {
        return errorHistoryRepository.countErrorHistoryWeekly();
    }
}
