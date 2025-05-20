package com.core.drm.base.batch.service;

import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.domain.entity.FileTempStorage;
import com.core.drm.crypto.service.CryptoResultService;
import com.core.drm.crypto.service.FileTempStorageService;
import com.core.drm.crypto.util.ListUtilsKt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempFileDeleteService {

    private static final int DELETE_SIZE = 1000;

    private final CryptoResultService cryptoResultService;
    private final FileTempStorageService fileTempStorageService;

    public void fileDeleteProcess() {
        List<FileTempStorage> tempFiles = fileTempStorageService.findTempFileListForDelete();
        Set<UUID> requestIds = new HashSet<>();
        for (FileTempStorage tempFile : tempFiles) {
            requestIds.add(tempFile.getFileRequest().getRequestId());
            new TempFile(tempFile.getFileSavePath()).delete();
        }
        resultUpdateFileStateToRemoved(new ArrayList<>(requestIds));
    }

    private void resultUpdateFileStateToRemoved(List<UUID> requestIds) {
        List<List<UUID>> partitionIDs = ListUtilsKt.partition(requestIds, DELETE_SIZE);
        for (List<UUID> list : partitionIDs) {
            cryptoResultService.updateFileStateToRemoved(list);
        }
    }

}
