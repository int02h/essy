package com.dpforge.essy.proxy;

import com.dpforge.essy.engine.HttpResponse;
import spark.Response;

import java.util.Map;

class ResponseFiller {
    void fillExceptContent(final Response response, final HttpResponse with) {
        for (Map.Entry<String, String> entry : with.getHeaders().entrySet()) {
            response.header(entry.getKey(), entry.getValue());
        }
        response.type(with.getContentType());
    }
}
