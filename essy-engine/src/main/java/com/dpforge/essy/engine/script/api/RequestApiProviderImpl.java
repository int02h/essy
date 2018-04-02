package com.dpforge.essy.engine.script.api;

import com.dpforge.essy.engine.sugar.api.CompressApi;
import com.dpforge.essy.engine.sugar.api.RequestApiProvider;
import com.dpforge.essy.engine.sugar.api.RequestExecutionApi;

class RequestApiProviderImpl implements RequestApiProvider {

    private final RequestExecutionApiImpl requestExecutionApi;

    private final CompressApiImpl compressApi = new CompressApiImpl();

    RequestApiProviderImpl(final ScriptApiContext context) {
        requestExecutionApi = new RequestExecutionApiImpl(context);
    }

    @Override
    public RequestExecutionApi getRequestExecutionApi() {
        return requestExecutionApi;
    }

    @Override
    public CompressApi getCompressApi() {
        return compressApi;
    }
}
