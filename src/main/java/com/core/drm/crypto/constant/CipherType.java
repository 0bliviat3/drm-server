package com.core.drm.crypto.constant;

import lombok.Getter;

@Getter
public enum CipherType {

    ENCRYPT("암호화"),
    DECRYPT("복호화");

    private final String description;

    CipherType(final String description) {
        this.description = description;
    }
}
