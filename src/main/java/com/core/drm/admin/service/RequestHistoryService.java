package com.core.drm.admin.service;

import com.core.drm.admin.repository.RequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestHistoryService {

    private final RequestHistoryRepository requestHistoryRepository;
}
