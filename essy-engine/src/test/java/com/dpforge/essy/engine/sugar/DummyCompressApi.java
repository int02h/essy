package com.dpforge.essy.engine.sugar;

import com.dpforge.essy.engine.sugar.api.CompressApi;

import java.io.IOException;

class DummyCompressApi implements CompressApi {
    @Override
    public byte[] gzip(final byte[] data) throws IOException {
        byte sum = 0;
        for (final byte b : data) {
            sum += b;
        }
        return new byte[]{sum};
    }

    @Override
    public byte[] ungzip(final byte[] compressed) throws IOException {
        byte[] res = new byte[2 * compressed.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = compressed[i % compressed.length];
        }
        return res;
    }
}
