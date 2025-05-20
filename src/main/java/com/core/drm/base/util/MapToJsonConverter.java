package com.core.drm.base.util;

import com.core.drm.base.exception.ConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.core.drm.base.constant.errormessage.ConvertExceptionMessage.FAIL_CONVERT_COLUMN;
import static com.core.drm.base.constant.errormessage.ConvertExceptionMessage.FAIL_CONVERT_MAP;

@Slf4j
@Converter(autoApply = false)
public class MapToJsonConverter implements AttributeConverter<Map<String, String>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, String> stringStringMap) {

        try {
            return new ObjectMapper().writeValueAsString(stringStringMap);
        } catch (JsonProcessingException e) {
            throw new ConvertException(FAIL_CONVERT_COLUMN, e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        try {
            return new ObjectMapper().readValue(s, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new ConvertException(FAIL_CONVERT_MAP, e);
        }
    }
}
