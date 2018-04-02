package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.sugar.api.ResponseApiProvider;

import javax.annotation.Nonnull;
import java.io.IOException;

public class HttpResponseSugar {

    private static final String GZIP = "gzip";

    private final HttpHeaders headers = HttpHeaders.create();
    private final Content content = Content.create();
    private final ResponseApiProvider apiProvider;

    private String contentType;

    private HttpResponseSugar(final ResponseApiProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    @Nonnull
    public Content getContent() {
        return content;
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

    public HttpResponse buildResponse() {
        return HttpResponse.newBuilder()
                .headers(headers.buildMap())
                .content(content.asBytes())
                .contentType(contentType)
                .build();
    }

    public static HttpResponseSugar wrap(final HttpResponse response, final ResponseApiProvider apiProvider) {
        final HttpResponseSugar sugar = new HttpResponseSugar(apiProvider);
        sugar.headers.setMap(response.getHeaders());
        sugar.contentType = response.getContentType();
        sugar.content.setBytes(response.getContent());
        return sugar;
    }
}
