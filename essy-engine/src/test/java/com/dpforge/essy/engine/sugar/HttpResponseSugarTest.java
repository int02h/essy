package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.sugar.api.ResponseApiProvider;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HttpResponseSugarTest {
    @Test
    public void wrap() {
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(testResponse(), apiProvider());
        assertEquals(1, sugar.getHeaders().size());
        assertEquals("test head", sugar.getHeaders().get("h1"));
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertEquals("test/content", sugar.getContentType());
    }

    @Test
    public void setContentType() {
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(testResponse(), apiProvider());
        assertEquals("test/content", sugar.getContentType());
        sugar.setContentType("example/content");
        assertEquals("example/content", sugar.getContentType());
    }

    @Test
    public void buildResponse() {
        final HttpResponse expected = testResponse();
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(expected, apiProvider());
        final HttpResponse actual = sugar.buildResponse();
        assertEquals(expected.getHeaders(), actual.getHeaders());
        assertArrayEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getContentType(), actual.getContentType());
    }

    @Test
    public void gzip() throws IOException {
        final HttpResponse expected = testResponse();
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(expected, apiProvider());
        sugar.gzip();
        assertArrayEquals(new byte[]{6}, sugar.getContent().asBytes());
        assertTrue(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    @Test
    public void alreadyGzipped() throws IOException {
        final HttpResponse expected = testResponse();
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(expected, apiProvider());
        sugar.getHeaders().set("Content-Encoding", "gzip");
        sugar.gzip();
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertEquals("gzip", sugar.getHeaders().get("Content-Encoding"));
    }

    @Test
    public void ungzip() throws IOException {
        final HttpResponse expected = testResponse();
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(expected, apiProvider());
        sugar.getHeaders().set("Content-Encoding", "gzip");
        sugar.ungzip();
        assertArrayEquals(new byte[]{1, 2, 3, 1, 2, 3}, sugar.getContent().asBytes());
        assertFalse(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    @Test
    public void alreadyUngzipped() throws IOException {
        final HttpResponse expected = testResponse();
        final HttpResponseSugar sugar = HttpResponseSugar.wrap(expected, apiProvider());
        sugar.ungzip();
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertFalse(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    private static HttpResponse testResponse() {
        return HttpResponse.newBuilder()
                .header("h1", "test head")
                .content(new byte[]{1, 2, 3})
                .contentType("test/content")
                .build();
    }

    private static ResponseApiProvider apiProvider() {
        return DummyCompressApi::new;
    }
}