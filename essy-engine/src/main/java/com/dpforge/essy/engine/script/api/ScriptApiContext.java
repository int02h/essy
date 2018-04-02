package com.dpforge.essy.engine.script.api;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.script.RequestExecutor;
import com.dpforge.essy.engine.sugar.HttpRequestSugar;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;
import com.dpforge.essy.engine.sugar.api.RequestApiProvider;
import com.dpforge.essy.engine.sugar.api.ResponseApiProvider;

import java.net.MalformedURLException;

public class ScriptApiContext {
    private final RequestExecutor requestExecutor;
    private final RequestApiProvider requestApiProvider;
    private final ResponseApiProvider responseApiProvider;

    public ScriptApiContext(final RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        requestApiProvider = new RequestApiProviderImpl(this);
        responseApiProvider = new ResponseApiProviderImpl();
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public HttpRequestSugar wrapRequest(final HttpRequest request) throws MalformedURLException {
        return HttpRequestSugar.wrap(request, requestApiProvider);
    }

    public HttpResponseSugar wrapResponse(final HttpResponse response) {
        return HttpResponseSugar.wrap(response, responseApiProvider);
    }
}
