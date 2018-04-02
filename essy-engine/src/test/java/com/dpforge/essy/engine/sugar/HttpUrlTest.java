package com.dpforge.essy.engine.sugar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpUrlTest {

    @Test
    public void build() throws Exception {
        final String testUrl = "https://example.com:4567/aaa/bbb/ccc?p1=123&p2=hello";
        final HttpUrl url = HttpUrl.parse(testUrl);
        assertEquals(testUrl, url.build());
    }

    @Test
    public void parse() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com:4567/aaa/bbb/ccc?p1=123&p2=hello");
        assertEquals("https", url.getProtocol());
        assertEquals("example.com", url.getHost());
        assertEquals(4567, url.getPort());
        assertEquals("/aaa/bbb/ccc", url.getPath());
        assertEquals("123", url.getQuery().get("p1"));
        assertEquals("hello", url.getQuery().get("p2"));
    }

    @Test
    public void setProtocol() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa/bbb/ccc?p1=123&p2=hello");
        url.setProtocol("ftp");
        assertEquals("ftp", url.getProtocol());
        assertEquals("ftp://example.com/aaa/bbb/ccc?p1=123&p2=hello", url.build());
    }

    @Test
    public void setHost() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa/bbb/ccc?p1=123&p2=hello");
        url.setHost("sample.io");
        assertEquals("sample.io", url.getHost());
        assertEquals("https://sample.io/aaa/bbb/ccc?p1=123&p2=hello", url.build());
    }

    @Test
    public void setPort() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa");
        url.setPort(4321);
        assertEquals(4321, url.getPort());
        assertEquals("https://example.com:4321/aaa", url.build());
    }

    @Test
    public void setPath() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa/bbb/ccc?p1=123&p2=hello");
        url.setPath("/test");
        assertEquals("/test", url.getPath());
        assertEquals("https://example.com/test?p1=123&p2=hello", url.build());
    }

    @Test
    public void setPathNoSlash() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa/bbb/ccc?p1=123&p2=hello");
        url.setPath("test");
        assertEquals("/test", url.getPath());
        assertEquals("https://example.com/test?p1=123&p2=hello", url.build());
    }

    @Test
    public void setEmpty() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/aaa/bbb/ccc?p1=123&p2=hello");
        url.setPath("");
        assertEquals("/", url.getPath());
        assertEquals("https://example.com/?p1=123&p2=hello", url.build());
    }

    @Test
    public void modifyQuery() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/?p1=123&p2=hello");
        url.getQuery().put("p1", "321");
        url.getQuery().put("p2", "world");
        assertEquals("https://example.com/?p1=321&p2=world", url.build());
    }

    @Test
    public void addQueryParam() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/");
        url.getQuery().put("p1", "test");
        assertEquals("https://example.com/?p1=test", url.build());
    }

    @Test
    public void removeQueryParam() throws Exception {
        final HttpUrl url = HttpUrl.parse("https://example.com/?p1=test");
        url.getQuery().remove("p1");
        assertEquals("https://example.com/", url.build());
    }
}