package com.core.drm.crypto.aspect;

import com.core.drm.crypto.constant.ProcessState;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.service.CryptoHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.core.drm.crypto.constant.ProcessState.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CryptoHistoryAspect {

    private final CryptoHistoryService cryptoHistoryService;

    private void recordState(JoinPoint joinPoint, ProcessState state) {
        log.debug("record history: {}", state.name());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof FileRequest) {
                cryptoHistoryService.saveCryptoHistory((FileRequest) arg, state);
            }
        }
    }

    @Before("execution(com.core.drm.crypto.service.impl.DRMProcessServiceImpl.cryptProcess(..))")
    public void recordInProcess(JoinPoint joinPoint) {
        recordState(joinPoint, IN_PROCESS);
    }

    @AfterThrowing(pointcut = "execution(com.core.drm.crypto.service.impl.DRMProcessServiceImpl.cryptProcess(..))")
    public void recordFail(JoinPoint joinPoint) {
        recordState(joinPoint, FAIL);
    }

    @AfterReturning(pointcut = "execution(com.core.drm.crypto.service.impl.DRMProcessServiceImpl.cryptProcess(..))")
    public void recordSuccess(JoinPoint joinPoint) {
        recordState(joinPoint, SUCCESS);
    }

}
