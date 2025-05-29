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
        if (url.equals("/dec/file") || url.equals("/enc/file")) {
            FileRequest currentFileRequest = (FileRequest) ThreadLocalMapUtil.get(FILE_REQUEST);
            ThreadLocalMapUtil.clear();

            CryptoHistory cryptoHistory = cryptoHistoryService.findByRequestId(currentFileRequest);
            cryptoResultService.saveCryptoResult(cryptoHistory);
        }
    }

}
