package com.core.drm.crypto.service;

import com.core.drm.admin.dto.CryptoRequestDTO;
import com.core.drm.crypto.constant.CipherType;
import com.core.drm.crypto.domain.entity.FileRequest;
import com.core.drm.crypto.repository.FileRequestRepository;
import com.core.drm.crypto.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.core.drm.crypto.constant.errormessage.EntityExceptionMessage.NOT_FOUND_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileRequestService {

    private final FileRequestRepository fileRequestRepository;

    public FileRequest saveFileRequest(MultipartFile file, HttpServletRequest request, CipherType cipherType) {
        String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
        FileRequest fileRequest = FileRequest.builder()
                .file(file)
                .fileName(file.getOriginalFilename())
                .fileExtension(fileExtension)
                .requestIP(request.getRemoteAddr())
                .requestType(cipherType)
                .requestTime(LocalDateTime.now())
                .build();

        return fileRequestRepository.save(fileRequest);
    }

    public FileRequest findById(UUID requestId) {
        String errMessage = String.format(NOT_FOUND_ID.getMessage(), FileRequest.class.getName(), requestId.toString());
        return fileRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(errMessage));
    }

    public Page<FileRequest> findAll(Pageable pageable) {
        return fileRequestRepository.findAll(pageable);
    }

    public List<CryptoRequestDTO> countDailyRequestGroupByType() {
        return fileRequestRepository.countRequestByTypeToday();
    }

}
