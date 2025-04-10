package com.core.drm.crypto.util;

import com.core.drm.crypto.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileUtil {

    private FileUtil() {
    }

    /*
    UUID 사용으로 임시파일명 생성
    파일 업로드시 파일직접 사용 방지용 보안처리
     */
    public static String generateFileName(String fileName) {
        String extension = getFileExtension(fileName);
        return String.format("%s.%s", UUID.randomUUID(), extension);
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
    파일 임시 저장
     */
    public static String saveTempFile(MultipartFile file) {
        //generate fileName
        String saveFileName = generateFileName(file.getName());

        //임시저장 경로 가져오기
        String tempPath = PropertiesUtil
                .getApplicationProperty("temp.file.save.path");
        //임시경로 + 임시파일명으로 저장
        String fullPath = tempPath + saveFileName;
        try (FileOutputStream outputStream = new FileOutputStream(fullPath)) {
            outputStream.write(file.getBytes());
        } catch (IOException e) {
            throw new FileException("[ERROR] 파일 임시저장 에러", e);
        }
        //임시경로 + 임시파일명 리턴
        return fullPath;
    }
}
