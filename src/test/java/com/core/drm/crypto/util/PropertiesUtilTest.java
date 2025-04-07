package com.core.drm.crypto.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesUtilTest {

    @Test
    void 프로퍼티_불러오기_테스트() {
        assertThat("drm")
                .isEqualTo(PropertiesUtil.getApplicationProperty("spring.application.name"));
    }
}
