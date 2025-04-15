package com.core.drm.crypto.controller;

import com.core.drm.crypto.service.DRMProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class DRMController {

    private final DRMProcessService drmProcessService;

    @Autowired
    public DRMController(final DRMProcessService drmProcessService) {
        this.drmProcessService = drmProcessService;
    }

    @PostMapping(
            value = "/enc/file",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<InputStreamResource> encryptFile(MultipartFile file) {
        InputStream inputStream = drmProcessService.encryptFile(file);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        String encodeFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"enc_" + encodeFileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

    @PostMapping(
            value = "/dec/file",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<InputStreamResource> decryptFile(MultipartFile file) {
        InputStream inputStream = drmProcessService.decryptFile(file);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        String encodeFileName = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dec_" + encodeFileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

}
