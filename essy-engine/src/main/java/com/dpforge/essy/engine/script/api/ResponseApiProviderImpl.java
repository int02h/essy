package com.dpforge.essy.engine.script.api;

import com.dpforge.essy.engine.sugar.api.CompressApi;
import com.dpforge.essy.engine.sugar.api.ResponseApiProvider;

class ResponseApiProviderImpl implements ResponseApiProvider {

    private final CompressApiImpl compressApi = new CompressApiImpl();

    @Override
    public CompressApi getCompressApi() {
        return compressApi;
    }
}
