package com.dpforge.essy.engine.sugar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

class JsonParser {
    private final ObjectMapper mapper;

    private final TypeReference<HashMap<String, Object>> mapTypeRef = new TypeReference<HashMap<String, Object>>() {
    };

    JsonParser() {
        this(new ObjectMapper());
    }

    // for testing purposes
    JsonParser(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    Map<String, Object> toJson(final byte[] data) {
        try {
            return mapper.readValue(data, mapTypeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    byte[] fromJson(final Map<String, Object> map) {
        try {
            return mapper.writeValueAsBytes(map);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
