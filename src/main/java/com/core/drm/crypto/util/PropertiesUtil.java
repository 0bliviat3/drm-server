package com.core.drm.crypto.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PropertiesUtil {

    private PropertiesUtil() {
    }

    private static Properties getProperties(String type) {
        Properties properties = new Properties();

        try (
                BufferedReader resourceReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new ClassPathResource(type).getInputStream()
                                )
                        )
        ) {
            properties.load(resourceReader);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("[ERROR] %s 로드 실패: %s", type, e.getMessage()));
        }
    }

    public static String getApplicationProperty(String key) {
        return Optional.of(getProperties("application.properties").getProperty(key))
                .orElse("등록되지 않은 프로퍼티"); //TODO: 상수처리
    }

    public static String getDBProperty(String key) {
        return Optional.of(getProperties("db.properties").getProperty(key))
                .orElse("등록되지 않은 프로퍼티"); //TODO: 상수처리
    }
}
