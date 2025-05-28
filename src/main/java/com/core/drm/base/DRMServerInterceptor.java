package com.core.drm.base;

import com.core.drm.admin.dto.RequestHistoryDTO;
import com.core.drm.admin.service.RequestHistoryService;
import com.core.drm.crypto.service.ResponseProcessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DRMServerInterceptor implements HandlerInterceptor {

    private final ResponseProcessService responseProcessService;
    private final RequestHistoryService requestHistoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("=====pre handle======");
        saveRequest(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void saveRequest(HttpServletRequest request) {
        String userId = (String) Optional.ofNullable(request.getSession().getAttribute("userId"))
                .orElse("guest"); //세션이 없다면 guest 처리
        String requestIP = request.getRemoteAddr();
        String requestURL = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();
        RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO(
                null,
                userId,
                requestIP,
                requestURL,
                requestTime);
        requestHistoryService.saveRequestHistory(requestHistoryDTO);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("=====post handle======");
        responseProcessService.saveCryptoResponse(request.getRequestURI());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
