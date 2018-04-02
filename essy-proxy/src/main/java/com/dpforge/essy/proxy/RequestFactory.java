package com.dpforge.essy.proxy;

import com.dpforge.essy.engine.HttpRequest;
import spark.Request;

class RequestFactory {
    HttpRequest convert(final Request request) {
        final HttpRequest.Builder builder = HttpRequest.newBuilder()
                .url(request.url())
                .method(request.requestMethod())
                .content(getRequestContent(request))
                .contentType(request.contentType());

        for (String name : request.headers()) {
            builder.header(name, request.headers(name));
        }

        return builder.build();
    }

    private byte[] getRequestContent(final Request request) {
        return ("GET".equals(request.requestMethod())) ? null : request.bodyAsBytes();
    }
}
