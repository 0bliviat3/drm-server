package com.core.drm.crypto.dto;

public record ExceptionResponse(String eventTime, String code, String message, String fileName) {
}
