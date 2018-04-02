package com.dpforge.essy.engine.script;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ScriptEngineTest {
    @Test
    public void executeRequest() throws ScriptExecutionException {
        final ScriptEngine engine = new ScriptEngine(new DummyRequestExecutor());
        final HttpResponse response = engine.execute(testRequest(), "executeRequest()");
        assertNotNull(response);
        assertArrayEquals(new byte[]{1, 1, 2, 3, 5, 8}, response.getContent());
    }

    @Test
    public void emptyScript() throws ScriptExecutionException {
        final ScriptEngine engine = new ScriptEngine(new DummyRequestExecutor());
        final HttpResponse response = engine.execute(testRequest(), "");
        assertNull(response);
    }

    @Test(expected = ScriptExecutionException.class)
    public void errorScript() throws ScriptExecutionException {
        final ScriptEngine engine = new ScriptEngine(new DummyRequestExecutor());
        engine.execute(testRequest(), "def a = 2 * b");
    }

    @Test
    public void logs() throws Exception {
        final ScriptEngine engine = new ScriptEngine(new DummyRequestExecutor());
        final StringWriter writer = new StringWriter();
        engine.execute(testRequest(), "print 'Hello World'", writer);
        assertEquals("Hello World", writer.toString());
    }

    private static HttpRequest testRequest() {
        return HttpRequest.newBuilder()
                .url("https://example.com")
                .method("POST")
                .content(new byte[]{1, 1, 2, 3, 5, 8})
                .build();
    }

    public static class DummyRequestExecutor implements RequestExecutor {

        @Override
        public HttpResponse execute(final HttpRequest request) throws IOException {
            return HttpResponse.newBuilder()
                    .headers(request.getHeaders())
                    .content(request.getContent())
                    .contentType(request.getContentType())
                    .build();
        }
    }
}