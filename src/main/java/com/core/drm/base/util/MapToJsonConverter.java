package com.core.drm.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Converter(autoApply = false)
public class MapToJsonConverter implements AttributeConverter<Map<String, String>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, String> stringStringMap) {

        try {
            return new ObjectMapper().writeValueAsString(stringStringMap);
        } catch (JsonProcessingException e) {
            //TODO: 예외 처리
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        try {
            return new ObjectMapper().readValue(s, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            //TODO: 예외 처리
            throw new RuntimeException(e);
        }
    }
}
