package com.core.drm.crypto.util;

import com.core.drm.crypto.constant.ThreadKey;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMapUtil {

    private ThreadLocalMapUtil() {
    }

    private static final ThreadLocal<Map<String, Object>> threadLocalMap =
            ThreadLocal.withInitial(HashMap::new);

    public static void put(ThreadKey key, Object value) {
        threadLocalMap.get().put(key.name(), value);
    }

    public static Object get(ThreadKey key) {
        return threadLocalMap.get().get(key.name());
    }

    public static void clear() {
        threadLocalMap.remove();
    }


}
