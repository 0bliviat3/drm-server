package com.core.drm.crypto.constant;

import lombok.Getter;

@Getter
public enum FileHeaderKey {
    SIGNATURE(12),
    KEY(256),
    IV(12);

    private final int length;

    FileHeaderKey(int length) {
        this.length = length;
    }

}
