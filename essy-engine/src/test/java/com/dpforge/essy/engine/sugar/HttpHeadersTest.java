package com.dpforge.essy.engine.sugar;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpHeadersTest {

    @Test
    public void get() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value"));
        assertEquals("value", headers.get("name"));
    }

    @Test
    public void getMulti() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1, value2, value3"));
        assertEquals("value1, value2, value3", headers.get("name"));
    }

    @Test
    public void getAll() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value"));
        assertEquals(Collections.singletonList("value"), headers.getAll("name"));
    }

    @Test
    public void getAllMulti() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1, value2, value3"));
        assertEquals(Arrays.asList("value1", "value2", "value3"), headers.getAll("name"));
    }

    @Test
    public void getNonExisting() {
        final HttpHeaders headers = HttpHeaders.create();
        assertNull(headers.get("non-existing"));
    }

    @Test
    public void getAllNonExisting() {
        final HttpHeaders headers = HttpHeaders.create();
        assertNull(headers.getAll("non-existing"));
    }

    @Test
    public void set() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value");
        assertEquals("value", headers.get("name"));
    }

    @Test
    public void setMulti() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value1, value2, value3");
        assertEquals("value1, value2, value3", headers.get("name"));
        assertEquals(Arrays.asList("value1", "value2", "value3"), headers.getAll("name"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void setEmpty() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("empty", "");
        assertEquals("", headers.get("empty"));
        assertTrue(headers.getAll("empty").isEmpty());
    }

    @Test
    public void setAll() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.setAll("name", Arrays.asList("value1", "value2", "value3"));
        assertEquals(Arrays.asList("value1", "value2", "value3"), headers.getAll("name"));
    }

    @Test
    public void setMap() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value");
        headers.setMap(Collections.singletonMap("name1", "value1"));
        assertEquals("value", headers.get("name"));
        assertEquals("value1", headers.get("name1"));
    }

    @Test
    public void addValueToEmpty() throws Exception {
        final HttpHeaders headers = HttpHeaders.create();
        headers.addValue("name", "value");
        assertEquals("value", headers.get("name"));
    }

    @Test
    public void addValue() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1"));
        headers.addValue("name", "value2");
        assertEquals(Arrays.asList("value1", "value2"), headers.getAll("name"));
    }

    @Test
    public void addMap() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value1");

        final Map<String, String> map = new HashMap<>();
        map.put("name", "value2");
        map.put("test", "example");
        headers.addMap(map);

        assertEquals("value1, value2", headers.get("name"));
        assertEquals("example", headers.get("test"));
    }

    @Test
    public void remove() {
        final HttpHeaders headers = HttpHeaders.create();
        assertFalse(headers.remove("name"));
        headers.set("name", "value");
        assertTrue(headers.remove("name"));
        assertNull(headers.get("name"));
    }

    @Test
    public void removeValue() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1, value2, value3"));
        assertTrue(headers.removeValue("name", "value2"));
        assertEquals(Arrays.asList("value1", "value3"), headers.getAll("name"));
    }

    @Test
    public void removeValueFalse() {
        final HttpHeaders headers = HttpHeaders.create();
        assertFalse(headers.removeValue("name", "value2"));
    }

    @Test
    public void clear() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1, value2, value3"));
        headers.clear();
        assertNull("", headers.get("name"));
        assertNull(headers.getAll("name"));
        assertEquals(0, headers.size());
    }

    @Test
    public void nullValue() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", null);
        headers.setAll("name", null);
        headers.addValue("name", null);
        headers.removeValue("name", null);
        headers.setMap(null);
        headers.addMap(null);
    }

    @Test
    public void buildMap() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value1, value2");
        headers.set("test", "example");

        final Map<String, String> map = headers.buildMap();
        assertEquals("value1, value2", map.get("name"));
        assertEquals("example", map.get("test"));
    }

    @Test
    public void buildMapUnmodifiable() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value");

        final Map<String, String> map = headers.buildMap();
        map.put("name", "changed");

        assertEquals("value", headers.get("name"));
    }

    @Test
    public void contains() {
        final HttpHeaders headers = HttpHeaders.create();
        assertFalse(headers.contains("name"));
        headers.set("name", "value");
        assertTrue(headers.contains("name"));
    }

    @Test
    public void containsValue() {
        final HttpHeaders headers = HttpHeaders.create(Collections.singletonMap("name", "value1, value2, value3"));
        assertTrue(headers.containsValue("name", "value2"));
        assertFalse(headers.containsValue("name", "value4"));
    }

    @Test
    public void caseInsensitivity() {
        final HttpHeaders headers = HttpHeaders.create();
        headers.set("name", "value");
        assertEquals("value", headers.get("Name"));
        assertTrue(headers.contains("NAME"));
        assertTrue(headers.remove("NamE"));
    }
}