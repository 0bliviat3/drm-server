package com.core.drm.base.batch.service;

import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.domain.entity.FileTempStorage;
import com.core.drm.crypto.service.CryptoResultService;
import com.core.drm.crypto.service.FileTempStorageService;
import com.core.drm.crypto.util.ListUtilsKt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempFileDeleteService {

    private final CryptoResultService cryptoResultService;
    private final FileTempStorageService fileTempStorageService;

    public void fileDeleteProcess() {
        List<FileTempStorage> tempFiles = fileTempStorageService.findTempFileListForDelete();
        log.debug("임시파일목록 가져오기 성공!");
        List<UUID> requestIds = new ArrayList<>();
        for (FileTempStorage tempFile : tempFiles) {
            new TempFile(tempFile.getFileSavePath()).delete();
            requestIds.add(tempFile.getFileRequest().getRequestId());
        }
        resultUpdateFileStateToRemoved(requestIds);
    }

    private void resultUpdateFileStateToRemoved(List<UUID> requestIds) {
        //TODO: 상수처리
        log.debug("결과 테이블 업데이트 시작 ids: {}", requestIds.toString());
        List<List<UUID>> partitionIDs = ListUtilsKt.partition(requestIds, 1000);
        for (List<UUID> list : partitionIDs) {
            cryptoResultService.updateFileStateToRemoved(list);
        }
    }

}
