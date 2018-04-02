package com.dpforge.essy.engine.script.api;

import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.sugar.HttpRequestSugar;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;
import com.dpforge.essy.engine.sugar.api.RequestExecutionApi;

import java.io.IOException;

class RequestExecutionApiImpl implements RequestExecutionApi {

    private final ScriptApiContext context;

    RequestExecutionApiImpl(final ScriptApiContext context) {
        this.context = context;
    }

    @Override
    public HttpResponseSugar execute(final HttpRequestSugar requestSugar) throws IOException {
        final HttpResponse response = context.getRequestExecutor().execute(requestSugar.buildRequest());
        return context.wrapResponse(response);
    }
}
