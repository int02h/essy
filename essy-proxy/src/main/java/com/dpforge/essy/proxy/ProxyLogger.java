package com.dpforge.essy.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class ProxyLogger {
    private final Logger logger = LoggerFactory.getLogger(ProxyLogger.class);

    void logRequest(final Request request) {
        final StringBuilder builder = new StringBuilder("Request\n");
        builder.append("URL: ").append(request.url()).append('\n');
        builder.append("Method: ").append(request.requestMethod()).append('\n');
        builder.append("Headers:\n");
        formatHeaders(builder, request);
        builder.append("Body: ").append(encodeBody(request.bodyAsBytes()));
        logger.info(builder.toString());
    }

    public void logResponse(final Response response, final String content) {
        logResponse(response, content.getBytes(StandardCharsets.UTF_8));
    }

    public void logResponse(final Response response, @Nullable final byte[] content) {
        final StringBuilder builder = new StringBuilder("Response\n");
        builder.append("Status: ").append(response.status()).append('\n');
        builder.append("Headers:\n");
        formatHeaders(builder, response);
        builder.append("Body: ").append(encodeBody(content));
        logger.info(builder.toString());
    }

    void logScript(@Nullable final String script) {
        if (script != null) {
            logger.info("Script:\n{}", script);
        } else {
            logger.info("Script: ABSENT");
        }
    }

    void logScriptOutput(final String output) {
        logger.info("Script logs: {}", output);
    }

    private static String formatHeaders(final StringBuilder builder, final Request request) {
        for (String name : request.headers()) {
            builder.append('\t').append(name).append(": ").append(request.headers(name)).append('\n');
        }
        return builder.toString();
    }

    private static String formatHeaders(final StringBuilder builder, final Response response) {
        final HttpServletResponse raw = response.raw();
        for (String name : raw.getHeaderNames()) {
            builder.append('\t').append(name).append(": ").append(raw.getHeader(name)).append('\n');
        }
        return builder.toString();
    }

    private static String encodeBody(@Nullable final byte[] body) {
        if (body == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(body);
    }
}
