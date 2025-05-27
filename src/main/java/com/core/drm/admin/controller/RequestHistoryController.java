package com.core.drm.admin.controller;

import com.core.drm.admin.dto.RequestHistoryDTO;
import com.core.drm.admin.service.RequestHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestHistoryController {

    private final RequestHistoryService requestHistoryService;

    @GetMapping("/request-historys")
    public List<RequestHistoryDTO> findRequestHistory(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return requestHistoryService
                .findAll(pageRequest)
                .getContent();
    }
}
