package com.core.drm.crypto.exception;

import org.junit.jupiter.api.Test;

import static com.core.drm.crypto.constant.errormessage.CipherExceptionMessage.IMPOSSIBLE_TRY;
import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionTest {

    @Test
    void 예외_메세지_확인() {
        try {
            throw new CipherException(IMPOSSIBLE_TRY, "a", "a");
        } catch (CipherException e) {
            assertThat(e.getMessage()).isEqualTo("[ERROR] 처리불가능한 요청: a 문서에 a 시도");
        }
    }

}
