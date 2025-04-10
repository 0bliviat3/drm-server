package com.core.drm.crypto.util;

import org.junit.jupiter.api.Test;

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

}
