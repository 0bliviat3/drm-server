package com.core.drm.admin.service;

import com.core.drm.admin.domain.RequestHistory;
import com.core.drm.admin.dto.RequestCountDTO;
import com.core.drm.admin.dto.RequestHistoryDTO;
import com.core.drm.admin.repository.RequestHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestHistoryService {

    private final RequestHistoryRepository requestHistoryRepository;
    private final UserService userService;

    public RequestHistoryDTO saveRequestHistory(RequestHistoryDTO requestHistoryDTO) {
        RequestHistory requestHistory = RequestHistory.builder()
                .user(userService.findById(requestHistoryDTO.userId()))
                .requestIP(requestHistoryDTO.requestIP())
                .requestTime(requestHistoryDTO.requestTime())
                .build();

        return requestHistoryRepository.save(requestHistory).toDTO();
    }

    public RequestHistoryDTO findById(UUID requestId) {
        return requestHistoryRepository.findById(requestId)
                .orElseThrow(EntityNotFoundException::new).toDTO();
    }

    public Page<RequestHistoryDTO> findAll(Pageable pageable) {
        return requestHistoryRepository.findAll(pageable)
                .map(RequestHistory::toDTO);
    }

    public List<RequestCountDTO> findWeeklyHistory() {
        return requestHistoryRepository.countRequestHistoryWeekly();
    }
}
