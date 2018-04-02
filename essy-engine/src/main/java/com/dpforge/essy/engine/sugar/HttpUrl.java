package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.util.NoExceptionCall;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUrl {

    private final Map<String, String> query = new LinkedHashMap<>();

    private String protocol;
    private String host;
    private int port;
    private String path;

    private HttpUrl() {
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        if (path.isEmpty() || path.charAt(0) != '/') {
            this.path = "/" + path;
        } else {
            this.path = path;
        }
    }

    public String build() {
        final StringBuilder builder = new StringBuilder()
                .append(protocol)
                .append("://")
                .append(host);
        if (port >= 0) {
            builder.append(':').append(port);
        }
        builder.append(path);

        String separator = "?";
        for (Map.Entry<String, String> entry : query.entrySet()) {
            builder.append(separator).append(entry.getKey()).append("=").append(entry.getValue());
            separator = "&";
        }
        return builder.toString();
    }

    private static String decode(final String value) {
        return new NoExceptionCall<String>() {
            @Override
            protected String callUnsafe() throws Exception {
                return URLDecoder.decode(value, "UTF-8");
            }
        }.call();
    }

    static HttpUrl parse(final String url) throws MalformedURLException {
        final HttpUrl result = new HttpUrl();
        final URL parsed = new URL(url);
        result.protocol = parsed.getProtocol();
        result.host = parsed.getHost();
        result.port = parsed.getPort();
        result.path = parsed.getPath();

        final String query = parsed.getQuery();
        if (query != null) {
            final String[] pairs = query.split("&");
            for (String pair : pairs) {
                final String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    result.query.put(decode(keyValue[0]), decode(keyValue[1]));
                }
            }
        }
        return result;
    }
}
