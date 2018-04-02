package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.script.api.ScriptApiContext;
import com.dpforge.essy.engine.sugar.HttpRequestSugar;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;
import groovy.lang.Script;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

public abstract class ScriptApiBase extends Script {

    public HttpResponseSugar response() {
        return response((byte[]) null, null);
    }

    public HttpResponseSugar response(@Nullable final String content, @Nullable final String contentType) {
        final byte[] bytes = (content == null) ? null : content.getBytes(StandardCharsets.UTF_8);
        return response(bytes, contentType);
    }

    public HttpResponseSugar response(@Nullable final byte[] content, @Nullable final String contentType) {
        final HttpResponse response = HttpResponse.newBuilder()
                .content(content)
                .contentType(contentType)
                .build();
        return getApiContext().wrapResponse(response);
    }

    public boolean executeRequest() throws IOException {
        final HttpRequestSugar requestSugar = getRequestSugar();
        if (requestSugar != null) {
            getBinding().setVariable(ScriptEngine.RESPONSE_VARIABLE, requestSugar.execute());
            return true;
        }
        return false;
    }

    public void targetUrl(final String url) throws MalformedURLException {
        final HttpRequestSugar requestSugar = getRequestSugar();
        if (requestSugar != null) {
            requestSugar.setUrl(url);
            requestSugar.getHeaders().set("Host", requestSugar.getUrl().getHost());
        }
    }

    @Nullable
    private HttpRequestSugar getRequestSugar() {
        if (getBinding().hasVariable(ScriptEngine.REQUEST_VARIABLE)) {
            final Object obj = getBinding().getVariable(ScriptEngine.REQUEST_VARIABLE);
            if (obj instanceof HttpRequestSugar) {
                return (HttpRequestSugar) obj;
            }
        }
        return null;
    }

    private ScriptApiContext getApiContext() {
        return ((EngineBinding) getBinding()).getApiContext();
    }
}
