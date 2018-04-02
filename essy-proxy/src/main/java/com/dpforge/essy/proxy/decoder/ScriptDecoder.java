package com.dpforge.essy.proxy.decoder;

import spark.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.zip.GZIPInputStream;


public class ScriptDecoder {
    public String decode(final String encodedScript) throws ScriptDecoderException {
        final byte[] compressed = Base64.getDecoder().decode(encodedScript);
        try {
            return decompress(compressed);
        } catch (IOException e) {
            throw new ScriptDecoderException(e);
        }
    }

    private static String decompress(final byte[] compressed) throws IOException {
        try (final GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressed));
             final StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(gzipInput, stringWriter);
            return stringWriter.toString();
        }
    }
}
