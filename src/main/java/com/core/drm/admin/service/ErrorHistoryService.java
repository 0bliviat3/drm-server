package com.core.drm.admin.service;

import com.core.drm.admin.domain.ErrorHistory;
import com.core.drm.admin.dto.ErrorHistoryDTO;
import com.core.drm.admin.repository.ErrorHistoryRepository;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ErrorHistoryService {

    private final ErrorHistoryRepository errorHistoryRepository;

    public ErrorHistoryDTO saveException(Exception exception, String eventTime, ResponseMessage responseMessage) {
        ErrorHistory errorHistory = ErrorHistory.builder()
                .errorCode(responseMessage.getCode())
                .eventTime(eventTime)
                .returnMessage(responseMessage.getMessage())
                .errorMessage(exception.getMessage())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
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
}
