package com.core.drm.crypto.util;

import com.core.drm.crypto.exception.CipherException;
import com.core.drm.crypto.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    /*
    UUID 사용으로 임시파일명 생성
    파일 업로드시 파일직접 사용 방지용 보안처리
     */
    public static String generateFileName(String fileName) {
        String extension = getFileExtension(fileName);
        String newFileName = String.format("%s.%s", UUID.randomUUID(), extension);
        log.info("origin fileName: {}", fileName);
        log.info("file name: {}", newFileName);
        return newFileName;
    }

    /*
    파일 확장자 가져오기
     */
    public static String getFileExtension(String fileName) {
        int splitPoint = fileName.lastIndexOf('.') + 1;
        return fileName.substring(splitPoint);
    }

    /*
    파일 확장자 화이트리스트 검사
    확장자는 DB에서 관리할것
     */
    public static void validateFileExtension(String fileExtension, Set<String> whiteList) {
        if (!whiteList.contains(fileExtension)) {
            throw new FileException(String.format("[ERROR] %s는 확장자 화이트리스트에 없습니다.", fileExtension));
        }
    }

    /*
    파일 임시저장
     */
    private static void saveTempFile(String path, byte[] fileData) {
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            outputStream.write(fileData);
        } catch (IOException e) {
            throw new FileException("[ERROR] 파일 임시저장 에러", e);
        }
    }

    /*
    파일 임시 저장 (프로퍼티에 저장된 경로 사용)
    multipartFile
     */
    public static String saveTempFile(MultipartFile file, String tempPath) {
        //generate fileName
        String saveFileName = generateFileName(file.getOriginalFilename());
        //임시저장 경로 가져오기
        String filePath = Optional.ofNullable(tempPath)
                .orElse(PropertiesUtil.getApplicationProperty("temp.file.save.path"));
        //임시경로 + 임시파일명으로 저장
        String fullPath = filePath + saveFileName;

        try {
            saveTempFile(fullPath, file.getBytes());
        } catch (IOException e) {
            throw new FileException("[ERROR] 파일 임시저장 에러", e);
        }
        //임시경로 + 임시파일명 리턴
        return fullPath;
    }

    /*
    파일 임시 저장 (프로퍼티에 저장된 경로 사용)
    byte[]
     */
    public static String saveTempFile(byte[] fileData, String fileName, String tempPath) {
        //임시저장 경로 가져오기
        String filePath = Optional.ofNullable(tempPath)
                .orElse(PropertiesUtil.getApplicationProperty("temp.file.crypt.path"));
        String fullPath = filePath + fileName;
        saveTempFile(fullPath, fileData);
        return fullPath;
    }
}
