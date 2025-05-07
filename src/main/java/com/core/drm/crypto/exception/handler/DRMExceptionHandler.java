package com.core.drm.crypto.exception.handler;

import com.core.drm.crypto.dto.ExceptionResponse;
import com.core.drm.crypto.dto.FileExceptionResponse;
import com.core.drm.crypto.service.DRMExceptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ExceptionResponse handleRuntimeException(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.error("RuntimeException error handler");

        return drmExceptionService.wrapRuntimeException(ex, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public FileExceptionResponse handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.error("IllegalStateException error handler");

        return drmExceptionService.wrapException(ex, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public FileExceptionResponse handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.error("IllegalArgumentException error handler");
        log.error("request = {}", request.getRequestURI());

        return drmExceptionService.wrapException(ex, request);
    }
}
