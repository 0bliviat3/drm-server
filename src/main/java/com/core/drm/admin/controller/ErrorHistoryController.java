package com.core.drm.admin.controller;

import com.core.drm.admin.dto.ErrorCountDTO;
import com.core.drm.admin.dto.ErrorHistoryDTO;
import com.core.drm.admin.service.ErrorHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ErrorHistoryController {

    private final ErrorHistoryService errorHistoryService;

    @GetMapping("/error-historys")
    public List<ErrorHistoryDTO> findErrorHistory(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ErrorHistoryDTO> errorPages = errorHistoryService.findAll(pageRequest);

        return errorPages.getContent();
    }

    @GetMapping("/error-history/week")
    public List<ErrorCountDTO> countErrorHistoryWeekly() {
        return errorHistoryService.countErrorHistoryWeekly();
    }
}
