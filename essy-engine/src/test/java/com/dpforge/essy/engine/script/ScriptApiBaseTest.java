package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.script.api.ScriptApiContext;
import com.dpforge.essy.engine.sugar.HttpRequestSugar;
import com.dpforge.essy.engine.sugar.HttpResponseSugar;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ScriptApiBaseTest {

    ScriptApiContext context;
    ScriptApiBase api;

    @Before
    public void init() {
        context = new ScriptApiContext(request -> HttpResponse.newBuilder().content(new byte[]{1, 2, 3}).build());
        api = new DummyApiBase(context);
    }

    @Test
    public void responseNoArgs() {
        final HttpResponseSugar responseSugar = api.response();
        assertEquals(0, responseSugar.getHeaders().size());
        assertNull(responseSugar.getContent().asBytes());
        assertNull(responseSugar.getContentType());
    }

    @Test
    public void responseStringContent() {
        final HttpResponseSugar responseSugar = api.response("Hello", "application/test");
        assertEquals(0, responseSugar.getHeaders().size());
        assertEquals("Hello", responseSugar.getContent().asPlainText());
        assertEquals("application/test", responseSugar.getContentType());
    }

    @Test
    public void responseBytesContent() {
        final HttpResponseSugar responseSugar = api.response(new byte[]{1, 2, 3}, "application/test");
        assertEquals(0, responseSugar.getHeaders().size());
        assertArrayEquals(new byte[]{1, 2, 3}, responseSugar.getContent().asBytes());
        assertEquals("application/test", responseSugar.getContentType());
    }

    @Test
    public void executeRequestSuccess() throws IOException {
        api.getBinding().setVariable(ScriptEngine.REQUEST_VARIABLE, requestSugar());
        assertTrue(api.executeRequest());
        final HttpResponseSugar response = (HttpResponseSugar) api.getBinding().getVariable(ScriptEngine.RESPONSE_VARIABLE);
        assertArrayEquals(new byte[]{1, 2, 3}, response.getContent().asBytes());
    }

    @Test
    public void executeRequestFail() throws IOException {
        assertFalse(api.executeRequest());
        assertFalse(api.getBinding().hasVariable(ScriptEngine.RESPONSE_VARIABLE));
    }

    @Test
    public void targetUrl() throws MalformedURLException {
        api.getBinding().setVariable(ScriptEngine.REQUEST_VARIABLE, requestSugar());

        HttpRequestSugar requestSugar = (HttpRequestSugar) api.getBinding().getVariable(ScriptEngine.REQUEST_VARIABLE);
        assertEquals("https://test.com/", requestSugar.getUrl().build());

        api.targetUrl("ftp://example.io/");

        requestSugar = (HttpRequestSugar) api.getBinding().getVariable(ScriptEngine.REQUEST_VARIABLE);
        assertEquals("ftp://example.io/", requestSugar.getUrl().build());
    }

    private HttpRequestSugar requestSugar() throws MalformedURLException {
        final HttpRequest request = HttpRequest.newBuilder().url("https://test.com/").method("GET").build();
        return context.wrapRequest(request);
    }

    private static class DummyApiBase extends ScriptApiBase {

        DummyApiBase(final ScriptApiContext context) {
            setBinding(new EngineBinding(context));
        }

        @Override
        public Object run() {
            throw new UnsupportedOperationException();
        }
    }
}