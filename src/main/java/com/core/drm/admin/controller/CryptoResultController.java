package com.core.drm.admin.controller;

import com.core.drm.crypto.domain.entity.CryptoResult;
import com.core.drm.crypto.service.CryptoResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CryptoResultController {

    private final CryptoResultService cryptoResultService;

    @GetMapping("/crypto-results")
    public Page<CryptoResult> getCryptoResultList(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("processEndTime").descending());

        return cryptoResultService.findAll(pageRequest);
    }
}
