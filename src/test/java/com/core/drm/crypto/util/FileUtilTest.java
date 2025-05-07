package com.core.drm.crypto.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtilTest {

    @Test
    void 파일_이름_생성() {
        String fileExtension = "txt";
        String newName = FileUtil.generateFileName(fileExtension);
        System.out.println(newName);
    }

    @Test
    void 파일_확장자_분리() {
        String fileName = "a.a.aaa.txt";
        int splitPoint = fileName.lastIndexOf('.') + 1;
        String extension = fileName.substring(splitPoint);
        System.out.println(extension);
    }

    @Test
    void 디렉토리_확인() {
        String path = PropertiesUtil.getApplicationProperty("temp.file.save.path");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        String date = LocalDateTime.now().format(dateFormat);

        File file = new File(path + File.separator + date);

        if (!file.exists()) {
            file.mkdir();
        }
    }

}
