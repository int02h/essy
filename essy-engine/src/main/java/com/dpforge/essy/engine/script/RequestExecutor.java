package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;

import java.io.IOException;

public interface RequestExecutor {
    HttpResponse execute(HttpRequest request) throws IOException;
}
