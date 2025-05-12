package com.core.drm.crypto.service;

import com.core.drm.crypto.constant.ProcessState;
import com.core.drm.crypto.constant.ThreadKey;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.dto.ExceptionResponse;
import com.core.drm.crypto.dto.FileExceptionResponse;
import com.core.drm.crypto.exception.*;
import com.core.drm.crypto.util.ThreadLocalMapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.time.LocalDateTime;

import static com.core.drm.crypto.constant.ProcessState.FAIL;
import static com.core.drm.crypto.constant.ThreadKey.FILE_REQUEST;
import static com.core.drm.crypto.constant.errormessage.ResponseMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DRMExceptionService {

    //TODO: 에러 저장 서비스 추가
    private final CryptoHistoryService cryptoHistoryService;

    public void saveFail() {
        cryptoHistoryService.saveCryptoHistory((FileRequest) ThreadLocalMapUtil.get(FILE_REQUEST), FAIL);
        ThreadLocalMapUtil.clear();
    }

    public ExceptionResponse wrapRuntimeException(Exception exception, HttpServletRequest request) {
        saveFail();
        String eventTime = String.valueOf(LocalDateTime.now());
        ResponseMessage responseMessage = checkExceptionType(exception);

        return new ExceptionResponse(eventTime, responseMessage.getCode(), responseMessage.getMessage());
    }

    public FileExceptionResponse wrapException(Exception exception, HttpServletRequest request) {
        saveFail();
        String eventTime = String.valueOf(LocalDateTime.now());
        ResponseMessage responseMessage = checkExceptionType(exception);
        String fileName = getCurrentRequestFileName(request);

        return new FileExceptionResponse(eventTime, responseMessage.getCode(), responseMessage.getMessage(), fileName);
    }

    private String getCurrentRequestFileName(HttpServletRequest request) {
        log.debug("req = {}, content-type = {}", request.getRequestURI(), request.getContentType());
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
            return multipartRequest.getFile("sourceFile").getOriginalFilename();
        }
        return "no file";
    }

    private ResponseMessage checkExceptionType(Exception exception) {
        if (exception instanceof DRMException drmException) {
            return drmException.getResponseMessage();
        }
        return UNCHECKED_EXCEPTION;
    }

}
