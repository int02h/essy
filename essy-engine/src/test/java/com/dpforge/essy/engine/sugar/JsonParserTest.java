package com.dpforge.essy.engine.sugar;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class JsonParserTest {

    @Test
    public void toJson() {
        final String data = "{\"name\":\"John Smith\",\"age\":26}";
        final JsonParser parser = new JsonParser();
        final Map<String, Object> json = parser.toJson(data.getBytes(StandardCharsets.UTF_8));
        assertEquals("John Smith", json.get("name"));
        assertEquals(26, json.get("age"));
    }

    @Test(expected = UncheckedIOException.class)
    public void toJsonFail() {
        final String data = "{\"name\":\"John Smith\",\"age\":26}";
        final JsonParser parser = new JsonParser(new ObjectMapper() {
            @Override
            public <T> T readValue(byte[] src, TypeReference valueTypeRef) throws IOException {
                throw new IOException();
            }
        });
        parser.toJson(data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void fromJson() {
        final Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", "Ann Green");
        json.put("age", 45);

        final JsonParser parser = new JsonParser();
        final byte[] expected = "{\"name\":\"Ann Green\",\"age\":45}".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, parser.fromJson(json));
    }

    @Test(expected = UncheckedIOException.class)
    public void fromJsonFail() {
        final JsonParser parser = new JsonParser(new ObjectMapper() {
            @Override
            public byte[] writeValueAsBytes(Object value) throws IOException {
                throw new IOException();
            }
        });
        parser.fromJson(Collections.emptyMap());
    }
}