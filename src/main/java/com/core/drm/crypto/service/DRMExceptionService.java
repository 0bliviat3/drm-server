package com.core.drm.crypto.service;

import com.core.drm.admin.dto.ErrorHistoryDTO;
import com.core.drm.admin.service.ErrorHistoryService;
import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import com.core.drm.crypto.domain.entity.CryptoHistory;
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

    private final CryptoHistoryService cryptoHistoryService;
    private final CryptoResultService cryptoResultService;
    private final ErrorHistoryService errorHistoryService;

    public void saveFail() {
        FileRequest fileRequest = (FileRequest) ThreadLocalMapUtil.get(FILE_REQUEST);
        if (fileRequest == null) {
            return;
        }
        CryptoHistory cryptoHistory =
                cryptoHistoryService.saveCryptoHistory(fileRequest, FAIL);
        cryptoResultService.saveCryptoResult(cryptoHistory);
        ThreadLocalMapUtil.clear();
    }

    private ErrorHistoryDTO saveHistory(Exception exception) {
        String eventTime = String.valueOf(LocalDateTime.now());
        ResponseMessage responseMessage = checkExceptionType(exception);
        return errorHistoryService.saveException(exception, eventTime, responseMessage);
    }

    public ExceptionResponse wrapRuntimeException(Exception exception, HttpServletRequest request) {
        saveFail();
        ErrorHistoryDTO errorHistoryDTO = saveHistory(exception);
        return new ExceptionResponse(
                errorHistoryDTO.eventTime(),
                errorHistoryDTO.errorCode(),
                errorHistoryDTO.errorMessage());
    }

    public FileExceptionResponse wrapException(Exception exception, HttpServletRequest request) {
        saveFail();
        ErrorHistoryDTO errorHistoryDTO = saveHistory(exception);
        String fileName = getCurrentRequestFileName(request);

        return new FileExceptionResponse(
                errorHistoryDTO.eventTime(),
                errorHistoryDTO.errorCode(),
                errorHistoryDTO.errorMessage(),
                fileName);
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
