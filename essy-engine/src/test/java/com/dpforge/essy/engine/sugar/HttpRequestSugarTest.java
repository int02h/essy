package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.sugar.api.CompressApi;
import com.dpforge.essy.engine.sugar.api.RequestApiProvider;
import com.dpforge.essy.engine.sugar.api.RequestExecutionApi;
import com.dpforge.essy.engine.sugar.api.ResponseApiProvider;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HttpRequestSugarTest {

    @Test
    public void wrap() throws MalformedURLException {
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(testRequest(), apiProvider());
        assertEquals("https://example.com/test", sugar.getUrl().build());
        assertEquals("GET", sugar.getMethod());
        assertEquals(1, sugar.getHeaders().size());
        assertEquals("some value", sugar.getHeaders().get("h1"));
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertEquals("test/type", sugar.getContentType());
    }

    @Test
    public void setMethod() throws MalformedURLException {
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(testRequest(), apiProvider());
        assertEquals("GET", sugar.getMethod());
        sugar.setMethod("POST");
        assertEquals("POST", sugar.getMethod());
    }

    @Test
    public void setContentType() throws MalformedURLException {
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(testRequest(), apiProvider());
        assertEquals("test/type", sugar.getContentType());
        sugar.setContentType("example/test");
        assertEquals("example/test", sugar.getContentType());
    }

    @Test
    public void setUrl() throws MalformedURLException {
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(testRequest(), apiProvider());
        sugar.setUrl("ftp://test.io/");
        assertEquals("ftp://test.io/", sugar.getUrl().build());
    }

    @Test
    public void buildRequest() throws MalformedURLException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        final HttpRequest actual = sugar.buildRequest();
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getHeaders(), actual.getHeaders());
        assertArrayEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getContentType(), actual.getContentType());
    }

    @Test
    public void execute() throws IOException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        final HttpResponseSugar responseSugar = sugar.execute();
        assertEquals("Hello", responseSugar.getContent().asPlainText());
        assertEquals("test/type", responseSugar.getContentType());
    }

    @Test
    public void gzip() throws IOException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        sugar.gzip();
        assertArrayEquals(new byte[]{6}, sugar.getContent().asBytes());
        assertTrue(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    @Test
    public void alreadyGzipped() throws IOException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        sugar.getHeaders().set("Content-Encoding", "gzip");
        sugar.gzip();
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertEquals("gzip", sugar.getHeaders().get("Content-Encoding"));
    }

    @Test
    public void ungzip() throws IOException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        sugar.getHeaders().set("Content-Encoding", "gzip");
        sugar.ungzip();
        assertArrayEquals(new byte[]{1, 2, 3, 1, 2, 3}, sugar.getContent().asBytes());
        assertFalse(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    @Test
    public void alreadyUngzipped() throws IOException {
        final HttpRequest expected = testRequest();
        final HttpRequestSugar sugar = HttpRequestSugar.wrap(expected, apiProvider());
        sugar.ungzip();
        assertArrayEquals(new byte[]{1, 2, 3}, sugar.getContent().asBytes());
        assertFalse(sugar.getHeaders().containsValue("Content-Encoding", "gzip"));
    }

    private static HttpRequest testRequest() {
        return HttpRequest.newBuilder()
                .url("https://example.com/test")
                .method("GET")
                .header("h1", "some value")
                .content(new byte[]{1, 2, 3})
                .contentType("test/type")
                .build();
    }

    private static RequestApiProvider apiProvider() {
        final ResponseApiProvider responseApiProvider = () -> {
            throw new UnsupportedOperationException();
        };
        final RequestExecutionApi executionApi = requestSugar -> {
            final HttpResponse response = HttpResponse.newBuilder()
                    .contentType("test/type")
                    .content("Hello".getBytes(StandardCharsets.UTF_8))
                    .build();
            return HttpResponseSugar.wrap(response, responseApiProvider);
        };
        return new RequestApiProvider() {
            @Override
            public RequestExecutionApi getRequestExecutionApi() {
                return executionApi;
            }

            @Override
            public CompressApi getCompressApi() {
                return new DummyCompressApi();
            }
        };
    }
}