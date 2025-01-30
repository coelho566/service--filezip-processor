package com.filezip.processor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils<T> {

    private  T data;

    private final ObjectMapper objectMapper;

    public  Object jsonToObject(String json) {

        try {
            return objectMapper.readValue(json, data.getClass());
        } catch (JsonProcessingException e) {
            log.error("Erro json processing {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
