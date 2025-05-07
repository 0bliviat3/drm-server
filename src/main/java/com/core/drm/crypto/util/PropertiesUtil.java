package com.core.drm.crypto.util;

import com.core.drm.crypto.exception.PropertyException;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.core.drm.crypto.constant.errormessage.PropertyExceptionMessage.FAIL_LOAD;
import static com.core.drm.crypto.constant.errormessage.PropertyExceptionMessage.INVALID_PROPERTY;

public class PropertiesUtil {

    private static final String APP = "application.properties";
    private static final String DB = "application-db.properties";

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
            throw new PropertyException(FAIL_LOAD, e, type);
        }
    }

    public static String getApplicationProperty(String key) {
        return Optional.ofNullable(getProperties(APP).getProperty(key))
                .orElseThrow(PropertyException::new);
    }

    public static String getDBProperty(String key) {
        return Optional.ofNullable(getProperties(DB).getProperty(key))
                .orElseThrow(PropertyException::new);
    }
}
