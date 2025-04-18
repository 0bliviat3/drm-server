package com.core.drm.crypto.service;

import com.core.drm.crypto.constant.errormessage.ResponseMessage;
import com.core.drm.crypto.dto.ExceptionResponse;
import com.core.drm.crypto.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.core.drm.crypto.constant.errormessage.ResponseMessage.*;

@Slf4j
@Service
public class DRMExceptionService {

    //TODO: DB 서비스 추가

    public ExceptionResponse wrapException(Exception exception) {
        String eventTime = String.valueOf(LocalDateTime.now());
        ResponseMessage responseMessage = checkExceptionType(exception);
        String fileName = getCurrentRequestFileName();

        return new ExceptionResponse(eventTime, responseMessage.getCode(), responseMessage.getMessage(), fileName);
    }

    private String getCurrentRequestFileName() { //TODO: 테스트 해본 결과 요청은 찾아오지만 multipartRep 는 아님
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attrs.getRequest();

        log.debug("req = {}", request.getRequestURI());

        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            log.debug("현재 요청 가져옴!");
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
