package com.core.drm.crypto.dto;

public record FileExceptionResponse(String eventTime, String code, String message, String fileName) {
}
