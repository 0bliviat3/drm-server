package com.core.drm.admin.service;

import com.core.drm.admin.repository.ErrorHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorHistoryService {

    private final ErrorHistoryRepository errorHistoryRepository;
}
