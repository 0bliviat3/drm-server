package com.core.drm.crypto.service;

import com.core.drm.crypto.domain.entity.CryptoHistory;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.util.ThreadLocalMapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.core.drm.crypto.constant.ThreadKey.FILE_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseProcessService {

    private final CryptoHistoryService cryptoHistoryService;
    private final CryptoResultService cryptoResultService;

    public void saveCryptoResponse(String url) {
        log.debug("call save response, current req url: {}", url);
        //TODO: 암복호화 요청에 대한 응답리턴할 경우 => url list 로 처리 할것 (allow url table 필요)
        FileRequest currentFileRequest = (FileRequest) ThreadLocalMapUtil.get(FILE_REQUEST);
        ThreadLocalMapUtil.clear();

        CryptoHistory cryptoHistory = cryptoHistoryService.findByRequestId(currentFileRequest);
        cryptoResultService.saveCryptoResult(cryptoHistory);
    }

}
