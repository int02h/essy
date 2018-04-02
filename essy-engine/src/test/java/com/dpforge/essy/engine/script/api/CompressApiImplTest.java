package com.dpforge.essy.engine.script.api;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class CompressApiImplTest {
    @Test
    public void compressionTest() throws IOException {
        final CompressApiImpl api = new CompressApiImpl();
        final byte[] compressed = api.gzip("Hello Compressed World!".getBytes(StandardCharsets.UTF_8));
        assertEquals("Hello Compressed World!", new String(api.ungzip(compressed)));
    }
}