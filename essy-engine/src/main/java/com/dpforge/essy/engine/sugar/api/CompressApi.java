package com.dpforge.essy.engine.sugar.api;

import java.io.IOException;

public interface CompressApi {

    byte[] gzip(final byte[] data) throws IOException;

    byte[] ungzip(final byte[] compressed) throws IOException;
}
