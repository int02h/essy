package com.dpforge.essy.engine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    @Nonnull
    private final String url;

    @Nonnull
    private final String method;

    private final Map<String, String> headers;

    private final byte[] content;

    private final String contentType;

    private HttpRequest(final Builder builder) {
        url = builder.url;
        method = builder.method;
        headers = builder.headers;
        content = builder.content;
        contentType = builder.contentType;
    }

    @Nonnull
    public String getUrl() {
        return url;
    }

    @Nonnull
    public String getMethod() {
        return method;
    }

    @Nullable
    public String getHeader(final String name) {
        return headers.get(name);
    }

    @Nonnull
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Nullable
    public byte[] getContent() {
        return content;
    }

    @Nullable
    public String getContentType() {
        return contentType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, String> headers = new HashMap<>();
        private String url;
        private String method;
        private byte[] content;
        private String contentType;

        private Builder() {
        }

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public Builder method(final String method) {
            this.method = method;
            return this;
        }

        public Builder headers(final Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder header(final String name, final String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder content(final byte[] content) {
            this.content = content;
            return this;
        }

        public Builder contentType(final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpRequest build() {
            if (url == null) {
                throw new NullPointerException("url cannot be null");
            }
            if (method == null) {
                throw new NullPointerException("method cannot be null");
            }
            return new HttpRequest(this);
        }
    }
}
