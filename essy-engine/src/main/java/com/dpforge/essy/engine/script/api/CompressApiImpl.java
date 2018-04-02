package com.dpforge.essy.engine.script.api;

import com.dpforge.essy.engine.sugar.api.CompressApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class CompressApiImpl implements CompressApi {

    @Override
    public byte[] gzip(final byte[] data) throws IOException {
        try (final ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            try (final GZIPOutputStream gzipOutput = new GZIPOutputStream(byteOut);
                 final ByteArrayInputStream byteInput = new ByteArrayInputStream(data)) {
                copy(byteInput, gzipOutput);
            }
            return byteOut.toByteArray();
        }
    }

    @Override
    public byte[] ungzip(final byte[] compressed) throws IOException {
        try (final GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressed));
             final ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            copy(gzipInput, byteOut);
            return byteOut.toByteArray();
        }
    }

    private void copy(final InputStream input, final OutputStream output) throws IOException {
        final byte[] buffer = new byte[4 * 1024];
        int count;
        while ((count = input.read(buffer)) >= 0) {
            output.write(buffer, 0, count);
        }
    }
}
