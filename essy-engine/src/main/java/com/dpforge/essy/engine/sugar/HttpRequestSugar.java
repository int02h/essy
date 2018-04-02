package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.sugar.api.RequestApiProvider;

import java.io.IOException;
import java.net.MalformedURLException;

public class HttpRequestSugar {

    private static final String GZIP = "gzip";

    private final HttpHeaders headers = HttpHeaders.create();
    private final Content content = Content.create();
    private final RequestApiProvider apiProvider;

    private HttpUrl url;
    private String method;
    private String contentType;

    private HttpRequestSugar(final RequestApiProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public void setUrl(final String url) throws MalformedURLException {
        this.url = HttpUrl.parse(url);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public Content getContent() {
        return content;
    }

    public HttpResponseSugar execute() throws IOException {
        return apiProvider.getRequestExecutionApi().execute(this);
    }

    public void gzip() throws IOException {
        if (!headers.containsValue(HttpHeaders.CONTENT_ENCODING, GZIP)) {
            final byte[] compressed = apiProvider.getCompressApi().gzip(content.asBytes());
            content.setBytes(compressed);
            headers.addValue(HttpHeaders.CONTENT_ENCODING, GZIP);
        }
    }

    public void ungzip() throws IOException {
        if (headers.containsValue(HttpHeaders.CONTENT_ENCODING, GZIP)) {
            final byte[] data = apiProvider.getCompressApi().ungzip(content.asBytes());
            content.setBytes(data);
            headers.removeValue(HttpHeaders.CONTENT_ENCODING, GZIP);
        }
    }

    public HttpRequest buildRequest() {
        return HttpRequest.newBuilder()
                .url(url.build())
                .method(method)
                .headers(headers.buildMap())
                .content(content.asBytes())
                .contentType(contentType)
                .build();
    }

    public static HttpRequestSugar wrap(final HttpRequest request, final RequestApiProvider apiProvider)
            throws MalformedURLException {
        final HttpRequestSugar sugar = new HttpRequestSugar(apiProvider);
        sugar.content.setBytes(request.getContent());
        sugar.url = HttpUrl.parse(request.getUrl());
        sugar.method = request.getMethod();
        sugar.headers.setMap(request.getHeaders());
        sugar.contentType = request.getContentType();
        return sugar;
    }
}
