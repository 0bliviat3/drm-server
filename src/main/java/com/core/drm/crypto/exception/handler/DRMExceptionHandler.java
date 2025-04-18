package com.core.drm.crypto.exception.handler;

import com.core.drm.crypto.dto.ExceptionResponse;
import com.core.drm.crypto.service.DRMExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.EOFException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RestController
public class DRMExceptionHandler {

    private final DRMExceptionService drmExceptionService;

    @Autowired
    public DRMExceptionHandler(DRMExceptionService drmExceptionService) {
        this.drmExceptionService = drmExceptionService;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ExceptionResponse handleIllegalStateException(IllegalStateException ex) {

        log.error("IllegalStateException error handler");

        return drmExceptionService.wrapException(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException ex) {

        log.error("IllegalArgumentException error handler");

        return drmExceptionService.wrapException(ex);
    }
}
