package com.core.drm.crypto.exception.handler;

import com.core.drm.crypto.dto.ExceptionResponse;
import com.core.drm.crypto.dto.FileExceptionResponse;
import com.core.drm.crypto.service.DRMExceptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RestController
public class DRMExceptionHandler {

    private final DRMExceptionService drmExceptionService;

    @Autowired
    public DRMExceptionHandler(DRMExceptionService drmExceptionService) {
        this.drmExceptionService = drmExceptionService;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        log.error("RuntimeException error handler");

        return ResponseEntity.badRequest()
                .body(drmExceptionService.wrapRuntimeException(ex, request));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<FileExceptionResponse> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.error("IllegalStateException error handler");

        return ResponseEntity.internalServerError()
                .body(drmExceptionService.wrapException(ex, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FileExceptionResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.error("IllegalArgumentException error handler");
        log.error("request = {}", request.getRequestURI());

        return ResponseEntity.badRequest()
                .body(drmExceptionService.wrapException(ex, request));
    }
}
