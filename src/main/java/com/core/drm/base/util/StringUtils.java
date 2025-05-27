package com.core.drm.base.util;

public class StringUtils {

    private StringUtils() {
    }

    public static String limit(String target, int limit) {
        if (target.length() > limit) {
            return target.substring(0, limit);
        }
        return target;
    }
}
