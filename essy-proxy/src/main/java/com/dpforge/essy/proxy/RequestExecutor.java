package com.dpforge.essy.proxy;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class RequestExecutor {

    private final OkHttpClient client = new OkHttpClient();

    @Nullable
    HttpResponse execute(final HttpRequest request) throws IOException {
        final Response response = client.newCall(buildRequest(request)).execute();
        try (ResponseBody body = response.body()) {
            return HttpResponse.newBuilder()
                    .headers(getResponseHeaders(response))
                    .content(getResponseContent(body))
                    .contentType(getResponseContentType(body))
                    .build();
        }
    }

    private Map<String, String> getResponseHeaders(final Response response) {
        final Map<String, String> headers = new HashMap<>();
        for (String name : response.headers().names()) {
            headers.put(name, response.header(name));
        }
        return headers;
    }

    private byte[] getResponseContent(@Nullable final ResponseBody body) throws IOException {
        return (body == null) ? null : body.bytes();
    }

    private String getResponseContentType(@Nullable final ResponseBody body) {
        if (body != null) {
            final MediaType type = body.contentType();
            return (type == null) ? null : type.toString();
        }
        return null;
    }

    private static Request buildRequest(final HttpRequest request) {
        return new Request.Builder()
                .url(request.getUrl())
                .method(request.getMethod(), getRequestBody(request))
                .headers(getRequestHeaders(request))
                .build();
    }

    private static Headers getRequestHeaders(final HttpRequest request) {
        final Headers.Builder builder = new Headers.Builder();
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    @Nullable
    private static RequestBody getRequestBody(final HttpRequest request) {
        final byte[] content = request.getContent();
        if (content != null) {
            final MediaType type = (request.getContentType() == null) ? null : MediaType.parse(request.getContentType());
            return RequestBody.create(type, content);
        } else {
            return null;
        }
    }
}
