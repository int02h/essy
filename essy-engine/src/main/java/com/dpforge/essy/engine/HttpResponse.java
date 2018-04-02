package com.dpforge.essy.engine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final Map<String, String> headers;

    private final byte[] content;

    private final String contentType;

    private HttpResponse(final Builder builder) {
        headers = builder.headers;
        content = builder.content;
        contentType = builder.contentType;
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

        @Nullable
        private byte[] content;

        @Nullable
        private String contentType;

        private Builder() {
        }

        public Builder headers(final Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder header(final String name, final String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder content(@Nullable final byte[] content) {
            this.content = content;
            return this;
        }

        public Builder contentType(@Nullable final String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
