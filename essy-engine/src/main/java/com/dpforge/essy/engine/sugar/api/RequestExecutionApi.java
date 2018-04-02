package com.dpforge.essy.engine.sugar.api;

import com.dpforge.essy.engine.sugar.HttpRequestSugar;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;

import java.io.IOException;

public interface RequestExecutionApi {
    HttpResponseSugar execute(HttpRequestSugar requestSugar) throws IOException;
}
