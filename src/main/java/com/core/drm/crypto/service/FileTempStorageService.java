package com.core.drm.crypto.service;

import com.core.drm.crypto.domain.TempFile;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.domain.entity.FileTempStorage;
import com.core.drm.crypto.repository.FileTempStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileTempStorageService {

    private final FileTempStorageRepository fileTempStorageRepository;

    public FileTempStorage saveFileTempStorage(FileRequest fileRequest, TempFile tempFile) {
        FileTempStorage fileTempStorage = FileTempStorage.builder()
                .fileRequest(fileRequest)
                .tempSaveFileName(tempFile.getName())
                .fileSavePath(tempFile.getPath())
                .saveTime(LocalDateTime.now())
                .build();

        return fileTempStorageRepository.save(fileTempStorage);
    }

    public List<FileTempStorage> findTempFileListForDelete() {
        return fileTempStorageRepository.findSavedYesterdayWithSuccessAndExistResult();
    }
}
