package com.core.drm.crypto.service;

import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import com.core.drm.crypto.dto.FileExceptionResponse;
import com.core.drm.crypto.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.time.LocalDateTime;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.*;

@Slf4j
@Service
public class DRMExceptionService {

    //TODO: DB 서비스 추가

    public FileExceptionResponse wrapException(Exception exception, HttpServletRequest request) {
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
            return multipartRequest.getFile("file").getOriginalFilename();
        }
        return "no file";
    }

    private ResponseMessage checkExceptionType(Exception exception) {
        if (exception instanceof CipherException) {
            return ABOUT_CIPHER;
        } else if (exception instanceof FileException) {
            return ABOUT_FILE;
        } else if (exception instanceof FileHeaderException) {
            return ABOUT_FILE_HEADER;
        } else if (exception instanceof FileParserException) {
            return ABOUT_FILE_PARSER;
        } else if (exception instanceof KeyException) {
            return ABOUT_KEY;
        }
        return UNCHECKED_EXCEPTION;
    }

}
