package com.dpforge.essy.proxy;

import com.dpforge.essy.engine.HttpRequest;
import com.dpforge.essy.engine.HttpResponse;
import com.dpforge.essy.engine.script.ScriptEngine;
import com.dpforge.essy.engine.script.ScriptExecutionException;
import com.dpforge.essy.proxy.decoder.ScriptDecoder;
import com.dpforge.essy.proxy.decoder.ScriptDecoderException;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class ProxyRoute implements Route {

    private static final String ESSY_SCRIPT_HEADER = "Essy-Script";

    private final RequestFactory requestFactory = new RequestFactory();

    private final ResponseFiller responseFiller = new ResponseFiller();

    private final RequestExecutor requestExecutor = new RequestExecutor();

    private final ScriptEngine engine = new ScriptEngine(requestExecutor::execute);

    private final ScriptDecoder scriptDecoder = new ScriptDecoder();

    private final ProxyLogger logger = new ProxyLogger();

    @Override
    public Object handle(final Request sparkRequest, final Response sparkResponse) throws Exception {
        logger.logRequest(sparkRequest);
        try {
            final byte[] content = handleInternal(sparkRequest, sparkResponse);
            logger.logResponse(sparkResponse, content);
            writeResponse(content, sparkResponse);
            return "";
        } catch (HandleException e) {
            sparkResponse.status(e.status);
            logger.logResponse(sparkResponse, e.result);
            return e.result;
        }
    }

    private void writeResponse(@Nullable final byte[] content, final Response sparkResponse) throws HandleException {
        if (content == null) {
            return;
        }
        try {
            sparkResponse.raw().getOutputStream().write(content);
        } catch (IOException e) {
            throw new HandleException(500, "Fail to write response output");
        }

    }

    @Nullable
    private byte[] handleInternal(final Request sparkRequest, final Response sparkResponse) throws HandleException {
        final String script = extractScript(sparkRequest);
        logger.logScript(script);
        if (script != null) {
            final HttpResponse httpResponse = executeScript(sparkRequest, script);
            return buildResponse(httpResponse, sparkResponse);
        } else {
            return "OK".getBytes(StandardCharsets.UTF_8);
        }
    }

    @Nullable
    private String extractScript(final Request request) throws HandleException {
        final String encodedScript = request.headers(ESSY_SCRIPT_HEADER);
        request.headers().remove(ESSY_SCRIPT_HEADER);
        if (encodedScript == null) {
            return null;
        }
        try {
            return scriptDecoder.decode(encodedScript);
        } catch (ScriptDecoderException e) {
            throw new HandleException(400, "Fail to decode script\n" + e.getCause());
        }
    }

    @Nullable
    private HttpResponse executeScript(final Request sparkRequest, final String script) throws HandleException {
        final HttpRequest httpRequest = requestFactory.convert(sparkRequest);
        try {
            final StringWriter logWriter = new StringWriter();
            final HttpResponse response = engine.execute(httpRequest, script, logWriter);
            logger.logScriptOutput(logWriter.toString());
            return response;
        } catch (ScriptExecutionException e) {
            throw new HandleException(422, e.getMessage() + "\nCause: " + e.getCause());
        }
    }

    @Nullable
    private byte[] buildResponse(@Nullable final HttpResponse httpResponse,
                                 @Nonnull final Response sparkResponse) throws HandleException {
        if (httpResponse == null) {
            throw new HandleException(422, "Script did not set valid response");
        }
        responseFiller.fillExceptContent(sparkResponse, httpResponse);
        return httpResponse.getContent();
    }

    private static class HandleException extends Exception {
        private final int status;

        private final String result;

        HandleException(final int status, final String result) {
            this.status = status;
            this.result = result;
        }
    }
}
